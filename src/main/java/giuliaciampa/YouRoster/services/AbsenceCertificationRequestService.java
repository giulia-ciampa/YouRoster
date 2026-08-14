package giuliaciampa.YouRoster.services;

import giuliaciampa.YouRoster.dto.requests.AbsenceCertificationRequestDTO;
import giuliaciampa.YouRoster.dto.requests.ReviewerNotesDTO;
import giuliaciampa.YouRoster.dto.requests.UpdateCertificationRequestDTO;
import giuliaciampa.YouRoster.dto.responses.AbsenceCertificationResponseDTO;
import giuliaciampa.YouRoster.dto.responses.AbsenceCertificationReviewResponseDTO;
import giuliaciampa.YouRoster.entities.AbsenceCertificationRequest;
import giuliaciampa.YouRoster.entities.Account;
import giuliaciampa.YouRoster.entities.RequestStatus;
import giuliaciampa.YouRoster.exceptions.BadRequestException;
import giuliaciampa.YouRoster.exceptions.NotFoundException;
import giuliaciampa.YouRoster.exceptions.UnauthorizedException;
import giuliaciampa.YouRoster.repositories.AbsenceCertificationRequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AbsenceCertificationRequestService {

    private final AbsenceCertificationRequestRepository absenceCertificationRequestRepository;
    private final UserService userService;


    public AbsenceCertificationRequestService(AbsenceCertificationRequestRepository absenceCertificationRequestRepository, UserService userService) {
        this.absenceCertificationRequestRepository = absenceCertificationRequestRepository;
        this.userService = userService;
    }

    //HELPER PER LA RISPOSTA
    private AbsenceCertificationResponseDTO mapToResponse(AbsenceCertificationRequest request) {
        return new AbsenceCertificationResponseDTO(
                request.getId(),
                request.getProtocolCode(),
                request.getStartDate(),
                request.getEndDate(),
                request.getTotalDays(),
                request.getIssueDate(),
                request.getCertificateUrl(),
                request.getCertificateType(),
                request.getRequestStatus(),
                request.getCreatedAt(),
                request.getEmployeeNotes()
        );
    }

    //HELPER RISPOSTA ADMIN O HR
    private AbsenceCertificationReviewResponseDTO mapToReviewerResponse(AbsenceCertificationRequest request) {

        return new AbsenceCertificationReviewResponseDTO(
                request.getId(),
                request.getProtocolCode(),
                request.getStartDate(),
                request.getEndDate(),
                request.getTotalDays(),
                request.getIssueDate(),
                request.getCertificateUrl(),
                request.getCertificateType(),
                request.getRequestStatus(),
                request.getCreatedAt(),
                request.getEmployeeNotes(),
                request.getReviewerNotes(),
                request.getReviewer() != null ? request.getReviewer().getId() : null,
                request.getReviewer() != null ? request.getReviewer().getName() + " " + request.getReviewer().getSurname() : "ADMIN"
        );


    }

    //HELPER ACCOUNT AUTENTICATO E' ADMIN O HR
    // Metodo di supporto privato per verificare che l'account sia un Admin
    private void validateAdminRole(Account currentAccount) {

        if (currentAccount == null || currentAccount.getRoles() == null ||
                currentAccount.getRoles().stream().noneMatch(role -> "ADMIN".equals(role.getName()) || "HR".equals(role.getName()))) {


            throw new UnauthorizedException("Accesso negato: solo l'amministratore può visualizzare queste richieste.");
        }
    }

    //1. NUOVA RICHIESTA CERTIFICATA
    public AbsenceCertificationResponseDTO newCertificationRequest(Account currentAccount, AbsenceCertificationRequestDTO payload) {

        //1. controllo numero protocollo
        if (absenceCertificationRequestRepository.existsByProtocolCode(payload.protocolCode())) {
            throw new BadRequestException("Il numero di protocollo inserito " + payload.protocolCode() + " esiste già. Digita un nuovo numero di protocollo.");
        }


        //2. controllo date
        if (payload.startDate().isAfter(payload.endDate())) {
            throw new BadRequestException("La data di inizio non può essere successiva alla data di fine.");
        }

        //3. upload certificato
        String certificateUrl = userService.uploadDocuments(payload.certificateFile(), "Certificato Inps");


        //4. popolamento entità
        AbsenceCertificationRequest newRequest = new AbsenceCertificationRequest();
        newRequest.setProtocolCode(payload.protocolCode());
        newRequest.setStartDate(payload.startDate());
        newRequest.setEndDate(payload.endDate());
        newRequest.setIssueDate(payload.issueDate());
        newRequest.setCertificateUrl(certificateUrl);
        newRequest.setCertificateType(payload.certificateType());
        newRequest.setEmployeeNotes(payload.employeeNotes());
        newRequest.setRequestStatus(RequestStatus.SENT);
        newRequest.setCreatedAt(LocalDateTime.now());

        //5. controllo sull'account autenticato
        if (currentAccount == null || currentAccount.getUser() == null) {
            throw new UnauthorizedException("Utente non autenticato correttamente.");
        }
        newRequest.setEmployee(currentAccount.getUser());

        //6. chiamo il metodo calcolo dei giorni
        newRequest.calculateTotalDays();

        //7. salvo
        AbsenceCertificationRequest savedRequest = absenceCertificationRequestRepository.save(newRequest);

        //8. risposta
        return mapToResponse(savedRequest);

    }

    //2. MODIFICA RICHIESTA SE LO STATO È ANCORA SENT
    public AbsenceCertificationResponseDTO updateRequestIfSentStatus(Account currentAccount, UpdateCertificationRequestDTO payload, UUID requestId) {
        //1 trova la richiesta
        AbsenceCertificationRequest request = absenceCertificationRequestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("La richiesta non è stata trovata"));

        //2 controlla stato se sent passa
        if (request.getRequestStatus() == RequestStatus.SENT) {

            // Aggiorna solo se il payload contiene un nuovo valore
            if (payload.protocolCode() != null && !payload.protocolCode().isBlank()) {
                request.setProtocolCode(payload.protocolCode());
            }

            if (payload.startDate() != null) {
                request.setStartDate(payload.startDate());
            }

            if (payload.endDate() != null) {
                request.setEndDate(payload.endDate());
            }

            if (payload.issueDate() != null) {
                request.setIssueDate(payload.issueDate());
            }

            if (payload.certificateFile() != null && !payload.certificateFile().isEmpty()) {
                if (request.getCertificateUrl() == null) {
                    String uploadedUrl = userService.uploadDocuments(payload.certificateFile(), "Certificato Inps");
                    request.setCertificateUrl(uploadedUrl);
                }
            }

            if (payload.certificateType() != null) {
                request.setCertificateType(payload.certificateType());
            }

            if (payload.employeeNotes() != null) {
                request.setEmployeeNotes(payload.employeeNotes());
            }

            request.calculateTotalDays();

        } else {
            throw new BadRequestException("La richiesta è già stata presa in carico. Apri una nuova pratica di rettifica.");
        }

        //3 aggiorna solo campi che vuoi aggiornare

        AbsenceCertificationRequest updatedRequest = absenceCertificationRequestRepository.save(request);

        //4. return
        return mapToResponse(updatedRequest);
    }


    //3. ELIMINA RICHIESTA SE LO STATO È ANCORA SENTI

    public void deleteRequestIfSentStatus(Account currentAccount, UUID requestId) {
        //1. trova richiesta
        AbsenceCertificationRequest request = absenceCertificationRequestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("La richiesta non è stata trovata."));

        //2. controlla status
        if (request.getRequestStatus() == RequestStatus.SENT) {
            absenceCertificationRequestRepository.delete(request);
        } else {
            throw new BadRequestException("La richiesta è già stata presa in carico. Apri una nuova pratica di rettifica.");
        }
    }

    //4. VISUALIZZA LE TUE RICHIESTE CERTIFICATE
    public Page<AbsenceCertificationResponseDTO> getMyCertifications(Account currentAccount, LocalDate startDate, LocalDate endDate, Pageable pageable) {

        Page<AbsenceCertificationRequest> requests;

        // Se l'utente ha inserito entrambe le date, filtro per range
        if (startDate != null && endDate != null) {
            requests = absenceCertificationRequestRepository.findByEmployeeAndStartDateBetween(
                    currentAccount.getUser(), startDate, endDate, pageable);
        } else if (startDate != null) {

            requests = absenceCertificationRequestRepository.findByEmployeeAndStartDate(currentAccount.getUser(), startDate, pageable);

        } else {
            // Altrimenti prendiamo tutte le sue richieste
            requests = absenceCertificationRequestRepository.findByEmployee(currentAccount.getUser(), pageable);
        }

        //map
        return requests.map(this::mapToResponse);
    }


    //5. METODO GET RICHIESTE CERTIFICATE CON FILTRI OPZIONALI
    public Page<AbsenceCertificationResponseDTO> getAllWithFilters(Account currentAccount, String name, RequestStatus status, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        // 1. Controllo di sicurezza
        validateAdminRole(currentAccount);

        // 2. Esecuzione query con la stringa di ricerca (nome o cognome)
        return absenceCertificationRequestRepository.findWithFilters(name, status, startDate, endDate, pageable)
                .map(this::mapToResponse);
    }


    //6. METODO GET RICHIESTE DA LAVORARE(STATO SENT)
    public Page<AbsenceCertificationResponseDTO> getPendingRequests(Account currentAccount, Pageable pageable) {
        // 1. Controllo di sicurezza
        validateAdminRole(currentAccount);

        // 2. Esecuzione query
        return absenceCertificationRequestRepository.findWithFilters(null, RequestStatus.SENT, null, null, pageable)
                .map(this::mapToResponse);

    }

    //7. ADMIN O HR VALUTANO LA RICHIESTA(APPROVA)
    public AbsenceCertificationReviewResponseDTO approveRequest(ReviewerNotesDTO payload, Account currentAccount, UUID requestId) {
        // 1. Controllo di sicurezza
        validateAdminRole(currentAccount);

        // 2. Trova la richiesta
        AbsenceCertificationRequest request = absenceCertificationRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("La richiesta non è stata trovata."));


        // 3. Verifica che la richiesta sia effettivamente in stato SENT (quindi ancora da valutare)
        if (request.getRequestStatus() != RequestStatus.SENT) {
            throw new BadRequestException("Questa richiesta è già stata valutata o non è in uno stato valido per la valutazione.");
        }
        

        //4. IMPOSTA REVISORE SOLO SE ESISTE UNO USER ASSOCIATO
        if (currentAccount.getUser() != null) {
            request.setReviewer(currentAccount.getUser());
        }

        request.setRequestStatus(RequestStatus.APPROVED);
        request.setReviewerNotes(payload.notes());


        // 5. Salva e ritorna il DTO mappato
        AbsenceCertificationRequest updatedRequest = absenceCertificationRequestRepository.save(request);

        return mapToReviewerResponse(updatedRequest);
    }


    //8. ADMIN O HR VALUTANO LA RICHIESTA(RIFIUTA)
    public AbsenceCertificationReviewResponseDTO rejectRequest(ReviewerNotesDTO payload, Account currentAccount, UUID requestId) {
        // 1. Controllo di sicurezza
        validateAdminRole(currentAccount);

        // 2. Trova la richiesta
        AbsenceCertificationRequest request = absenceCertificationRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("La richiesta non è stata trovata."));

        // 3. Verifica che la richiesta sia effettivamente in stato SENT (quindi ancora da valutare)
        if (request.getRequestStatus() != RequestStatus.SENT) {
            throw new BadRequestException("Questa richiesta è già stata valutata o non è in uno stato valido per la valutazione.");
        }


        //4. IMPOSTA REVISORE SOLO SE ESISTE UNO USER ASSOCIATO
        if (currentAccount.getUser() != null) {
            request.setReviewer(currentAccount.getUser());
        }

        request.setRequestStatus(RequestStatus.REJECTED);
        request.setReviewerNotes(payload.notes());


        // 5. Salva e ritorna il DTO mappato
        AbsenceCertificationRequest updatedRequest = absenceCertificationRequestRepository.save(request);
        return mapToReviewerResponse(updatedRequest);
    }
}
