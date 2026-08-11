package giuliaciampa.YouRoster.dto.responses;

import java.util.UUID;

public record ShiftTaskTitleResponseDTO(
        UUID id,
        String title,
        String description,
        boolean isActive
) {
}
