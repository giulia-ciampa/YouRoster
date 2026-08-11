package giuliaciampa.YouRoster.services;

import giuliaciampa.YouRoster.dto.requests.ShiftAssignmentDTO;
import giuliaciampa.YouRoster.dto.requests.UpdateShiftAssignmentDTO;
import giuliaciampa.YouRoster.dto.responses.ShiftAssignmentResponseDTO;
import giuliaciampa.YouRoster.emailTemplates.EmailTemplateBuilder;
import giuliaciampa.YouRoster.entities.AssignmentType;
import giuliaciampa.YouRoster.entities.Shift;
import giuliaciampa.YouRoster.entities.ShiftAssignment;
import giuliaciampa.YouRoster.entities.User;
import giuliaciampa.YouRoster.exceptions.BadRequestException;
import giuliaciampa.YouRoster.exceptions.NotFoundException;
import giuliaciampa.YouRoster.repositories.ShiftAssignmentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ShiftAssignmentService {
    private final ShiftAssignmentRepository shiftAssignmentRepository;
    private final UserService userService;
    private final ShiftService shiftService;
    private final EmailService emailService;
    @Value("${shiftUpdated.url}")
    private String userPageUrl;

    public ShiftAssignmentService(ShiftAssignmentRepository shiftAssignmentRepository, UserService userService, ShiftService shiftService, EmailService emailService) {
        this.shiftAssignmentRepository = shiftAssignmentRepository;
        this.userService = userService;
        this.shiftService = shiftService;
        this.emailService = emailService;
    }

    //HELPER
    public ShiftAssignmentResponseDTO toResponseDTO(ShiftAssignment assignment) {
        return new ShiftAssignmentResponseDTO(
                assignment.getId(),
                (assignment.getUser() != null) ? assignment.getUser().getName() : null,
                (assignment.getUser() != null) ? assignment.getUser().getSurname() : null,
                (assignment.getUser() != null) ? assignment.getUser().getAccount().getEmail() : null,
                (assignment.getShift() != null && assignment.getShift().getOffice() != null) ? assignment.getShift().getOffice().getName() : null,
                (assignment.getShift() != null) ? assignment.getShift().getStartTime() : null,
                (assignment.getShift() != null) ? assignment.getShift().getEndTime() : null,
                assignment.getShiftDate(),
                assignment.getAssignmentType()
        );
    }

    //1 CREA UN'ASSEGNAZIONE
    public ShiftAssignmentResponseDTO saveNewAssignment(ShiftAssignmentDTO payload) {
        //1. recupero l'utente
        User user = userService.findById(payload.userId());

        //2. recuper il turno

        Shift shift = shiftService.findById(payload.shiftId());

        //3. gestione del turno in base all'Assignment type
        if (payload.assignmentType() == AssignmentType.WORK) {
            if (payload.shiftId() == null) {
                throw new BadRequestException("L'assegnazione del turno è obbligatorio per i giorni di lavoro");
            }


            //verifico che il turno sia attivo
            if (!shift.isActive()) {
                throw new BadRequestException("Non si può assegnare un turno disattivato.");
            }

        } else {
            shift = null;
        } //ON_HOLIDAY, SICK, OFF

        //4. controllo se l'utente ha già un'assegnazione in quella data
        boolean alreadyAssigned = shiftAssignmentRepository.existsByUserAndShiftDate(user, payload.shiftDate());
        if (alreadyAssigned) {
            throw new BadRequestException("Esiste già un'assegnazione per questo utente in questa data.");
        }

        //5. creazione della nuova entità
        ShiftAssignment assignment = new ShiftAssignment();
        assignment.setUser(user);
        assignment.setShift(shift);
        assignment.setAssignmentType(payload.assignmentType());
        assignment.setShiftDate(payload.shiftDate());


        //6. salvataggio

        ShiftAssignment savedAssignment = shiftAssignmentRepository.save(assignment);

        return toResponseDTO(savedAssignment);
    }

    //2 MODIFICA UN'ASSEGNAZIONE(INVIO EMAIL ALLO USER)
    public ShiftAssignmentResponseDTO updateShiftAssignment(UUID id, UpdateShiftAssignmentDTO payload) {
        // 1. Recupero l'assegnazione esistente
        ShiftAssignment assignment = shiftAssignmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Assegnazione non trovata."));

        // 2. REGOLA: Controllo se il turno è già completato (passato)
        if (assignment.getShift() != null) {
            LocalDateTime shiftEndDateTime = LocalDateTime.of(assignment.getShiftDate(), assignment.getShift().getEndTime());


            // Se il momento attuale è successivo alla fine del turno, non si può più modificare
            if (LocalDateTime.now().isAfter(shiftEndDateTime)) {
                throw new BadRequestException("Non è possibile modificare un'assegnazione il cui turno è già completato.");
            }
        } else {// Se è un giorno di ferie/malattia passato (solo data, senza orario turno)
            if (assignment.getShiftDate().isBefore(LocalDate.now())) {
                throw new BadRequestException("Non è possibile modificare uno storico di ferie o malattia passato.");
            }
        }

        //3. Aggiornamento parziale
        if (payload.userId() != null) {
            User user = userService.findById(payload.userId());
            assignment.setUser(user);
        }

        if (payload.assignmentType() != null) {
            assignment.setAssignmentType(payload.assignmentType());
        }

        // Gestione logica del turno in base al tipo
        AssignmentType currentType = payload.assignmentType() != null ? payload.assignmentType() : assignment.getAssignmentType();

        if (currentType == AssignmentType.WORK) {
            if (payload.shiftId() != null) {
                Shift shift = shiftService.findById(payload.shiftId());
                assignment.setShift(shift);
            } else if (assignment.getShift() == null) {
                throw new BadRequestException("Il turno è obbligatorio per i giorni di lavoro.");
            }
        } else {
            // Se non è lavoro (es. ferie), rimuoviamo il turno associato
            assignment.setShift(null);
        }

        if (payload.shiftDate() != null) {
            assignment.setShiftDate(payload.shiftDate());
        }
        

        //4 salvataggio

        ShiftAssignment updatedAssignment = shiftAssignmentRepository.save(assignment);

        //5 invio email
        String htmlBody = EmailTemplateBuilder.buildShiftUpdated(assignment.getShiftDate(), assignment.getUser().getName(), assignment.getShift(), userPageUrl);
        emailService.sendHtmlEmail(assignment.getUser().getAccount().getEmail(), "[Turno modificato - YouRoster!]", htmlBody);

        return toResponseDTO(updatedAssignment);
    }

    //3 CANCELLA UN'ASSEGNAZIONE
    public void deleteShiftAssignment(UUID id) {
        ShiftAssignment shiftAssignment = shiftAssignmentRepository.findById(id).orElseThrow(() -> new NotFoundException("L'assegnazione del turno non è stata trovata"));

        //controllo temporale
        if (shiftAssignment.getShift() != null) {
            LocalDateTime shiftEndDateTime = LocalDateTime.of(shiftAssignment.getShiftDate(), shiftAssignment.getShift().getEndTime());

            if (LocalDateTime.now().isAfter(shiftEndDateTime)) {
                throw new BadRequestException("Non è possibile eliminare un'assegnazione per un turno già completato.");
            }
        } else {
            // Controllo per ferie/malattia
            if (shiftAssignment.getShiftDate().isBefore(LocalDate.now())) {
                throw new BadRequestException("Non è possibile eliminare uno storico di ferie o malattia passato.");
            }
        }

        shiftAssignmentRepository.delete(shiftAssignment);
    }

    //4 VISUALIZZA TUTTE LE ASSEGNAZIONI
    public Page<ShiftAssignmentResponseDTO> getAllAssignment(Pageable pageable) {

        Page<ShiftAssignment> assignmentsPage = shiftAssignmentRepository.findAll(pageable);

        return assignmentsPage.map(this::toResponseDTO);
    }

    //5 VISUALIZZA LE ASSEGNAZIONI PER DATA
    public Page<ShiftAssignmentResponseDTO> getAllAssignmentByDate(LocalDate shiftDate, Pageable pageable) {

        Page<ShiftAssignment> assignmentsPage = shiftAssignmentRepository.findByShiftDate(shiftDate, pageable);

        return assignmentsPage.map(this::toResponseDTO);
    }

    //6 VISUALIZZA LE ASSEGNAZIONI DA DATA X A DATA Y
    public Page<ShiftAssignmentResponseDTO> getAssignmentsBetweenDates(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<ShiftAssignment> assignmentsPage = shiftAssignmentRepository.findByShiftDateBetween(startDate, endDate, pageable);
        return assignmentsPage.map(this::toResponseDTO);
    }

    //7 VISUALIZZA LE PROPRIE ASSEGNAZIONI
    public Page<ShiftAssignmentResponseDTO> getMyAssignment(UUID userId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        User user = userService.findById(userId);

        Page<ShiftAssignment> assignmentsPage = shiftAssignmentRepository.findByUserAndShiftDateBetween(user, startDate, endDate, pageable);

        return assignmentsPage.map(this::toResponseDTO);
    }

    //8 VISUALIZZA UTENTI IN TURNO CON TE
    public List<ShiftAssignmentResponseDTO> getColleaguesOnMyShift(UUID userId, LocalDate shiftDate) {

        //1 recupero l'utente
        User user = userService.findById(userId);

        //2 cerco la sua assegnazione per quel giorno
        ShiftAssignment myShiftAssignment = shiftAssignmentRepository.findByUserAndShiftDate(user, shiftDate).orElseThrow(() -> new NotFoundException("Non hai nessuna assegnazione per questa data."));

        //3 se l'utente è in malattia, off, o ferie, quindi shift è null, non ha colleghi in turno
        if (myShiftAssignment.getShift() == null) {
            throw new BadRequestException("In questa data sei in " + myShiftAssignment.getAssignmentType() + ", non hai un turno di lavoro.");
        }
        //4 cerco gli altri dipendenti che hanno lo stesso turno, lo stesso giorno
        List<ShiftAssignment> colleaguesAssignments = shiftAssignmentRepository
                .findByShiftAndShiftDateAndUserNot(myShiftAssignment.getShift(), shiftDate, user);

        //5 return
        return colleaguesAssignments.stream()
                .map(this::toResponseDTO)
                .toList();

    }

}
