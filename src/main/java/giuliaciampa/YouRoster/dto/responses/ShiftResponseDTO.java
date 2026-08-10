package giuliaciampa.YouRoster.dto.responses;

import java.time.LocalTime;
import java.util.UUID;

public record ShiftResponseDTO(
        UUID id,
        String officeName,
        LocalTime startTime,
        LocalTime endTime,
        Boolean isActive
) {
}
