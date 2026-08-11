package giuliaciampa.YouRoster.services;

import giuliaciampa.YouRoster.dto.requests.ShiftTaskTitleRequestDTO;
import giuliaciampa.YouRoster.dto.requests.ShiftTaskTitleUpdateRequestDTO;
import giuliaciampa.YouRoster.dto.responses.ShiftTaskTitleResponseDTO;
import giuliaciampa.YouRoster.entities.ShiftTaskTitle;
import giuliaciampa.YouRoster.exceptions.AlreadyExistsException;
import giuliaciampa.YouRoster.exceptions.NotFoundException;
import giuliaciampa.YouRoster.repositories.ShiftTaskTitleRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ShiftTaskTitleService {
    private final ShiftTaskTitleRepository shiftTaskTitleRepository;

    public ShiftTaskTitleService(ShiftTaskTitleRepository shiftTaskTitleRepository) {
        this.shiftTaskTitleRepository = shiftTaskTitleRepository;
    }

    //1. CREA NUOVO TITOLO PER IL TASK
    public ShiftTaskTitleResponseDTO saveNewTaskTitle(ShiftTaskTitleRequestDTO payload) {

        if (shiftTaskTitleRepository.existsByTitle(payload.title())) {
            throw new AlreadyExistsException("il titolo della mansione che si vuole aggiungere esiste già");
        }

        ShiftTaskTitle shiftTaskTitle = new ShiftTaskTitle();
        shiftTaskTitle.setTitle(payload.title().toUpperCase());
        shiftTaskTitle.setDescription(payload.description());

        ShiftTaskTitle savedShiftTitle = shiftTaskTitleRepository.save(shiftTaskTitle);

        return new ShiftTaskTitleResponseDTO(savedShiftTitle.getId(), savedShiftTitle.getTitle(), savedShiftTitle.getDescription(), savedShiftTitle.isActive());
    }

    //2. MODIFICA TITOLO
    public ShiftTaskTitleResponseDTO updateTaskTitle(UUID id, ShiftTaskTitleUpdateRequestDTO payload) {
        ShiftTaskTitle foundedShiftTaskTitle = shiftTaskTitleRepository.findById(id).orElseThrow(() -> new NotFoundException("Il titolo della mansione non è stato trovato"));

        if (payload.title() != null) {
            if (!foundedShiftTaskTitle.getTitle().equals(payload.title()) && shiftTaskTitleRepository.existsByTitle(payload.title())) {
                throw new AlreadyExistsException("Esiste già un titolo della mansione con questo nome.");
            }
            foundedShiftTaskTitle.setTitle(payload.title().toUpperCase());
        }

        if (payload.description() != null) {
            foundedShiftTaskTitle.setDescription(payload.description());
        }

        ShiftTaskTitle updatedTaskTitle = shiftTaskTitleRepository.save(foundedShiftTaskTitle);

        return new ShiftTaskTitleResponseDTO(updatedTaskTitle.getId(), updatedTaskTitle.getTitle(), updatedTaskTitle.getDescription(), updatedTaskTitle.isActive());

    }

    //3. ELIMINA TITOLO TASK(SOFT)

    public ShiftTaskTitleResponseDTO deactivateTaskTitle(UUID id) {
        ShiftTaskTitle founded = shiftTaskTitleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Il titolo della mansione non è stato trovato"));


        founded.setActive(false);

        ShiftTaskTitle updated = shiftTaskTitleRepository.save(founded);

        return new ShiftTaskTitleResponseDTO(
                updated.getId(),
                updated.getTitle(),
                updated.getDescription(),
                updated.isActive()
        );

    }

    //4. RECUPERA TITOLO TASK ELIMINATO
    public ShiftTaskTitleResponseDTO reactivateTaskTitle(UUID id) {
        ShiftTaskTitle founded = shiftTaskTitleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Il titolo della mansione non è stato trovato"));


        founded.setActive(true);

        ShiftTaskTitle updated = shiftTaskTitleRepository.save(founded);

        return new ShiftTaskTitleResponseDTO(
                updated.getId(),
                updated.getTitle(),
                updated.getDescription(),
                updated.isActive()
        );

    }

}
