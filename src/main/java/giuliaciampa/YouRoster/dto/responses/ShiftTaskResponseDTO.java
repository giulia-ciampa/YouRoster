package giuliaciampa.YouRoster.dto.responses;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record ShiftTaskResponseDTO(
        UUID id,
        UUID shiftAssignmentId,
        LocalDate shiftDate,
        String assignmentType,
        LocalTime time,
        UUID taskTitleId,
        String taskTitleName

) {
}
