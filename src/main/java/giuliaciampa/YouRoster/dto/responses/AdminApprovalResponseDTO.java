package giuliaciampa.YouRoster.dto.responses;

import java.time.LocalDateTime;

public record AdminApprovalResponseDTO(
        String message,
        LocalDateTime timestamp
) {
}
