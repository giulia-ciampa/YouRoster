package giuliaciampa.YouRoster.dto.requests;

import giuliaciampa.YouRoster.entities.AssignmentType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record ShiftAssignmentDTO(
        @NotNull(message = "l'utente è obbligatorio")
        UUID userId,
        @NotNull(message = "il turno è obbligatorio")
        UUID shiftId,
        @NotNull(message = "la data è obbligatoria")
        LocalDate shiftDate,
        @NotNull(message = "il tipo di assegnazione è obbligatoria")
        AssignmentType assignmentType
) {
}
