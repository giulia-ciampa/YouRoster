package giuliaciampa.YouRoster.dto.responses;

import giuliaciampa.YouRoster.entities.AssignmentType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record ShiftAssignmentResponseDTO(
        UUID id,
        String userName,
        String userSurname,
        String userEmail,
        String officeName,
        LocalTime startTime,
        LocalTime endTime,
        LocalDate shiftDate,
        AssignmentType assignmentType,
        String tasks
) {
}
