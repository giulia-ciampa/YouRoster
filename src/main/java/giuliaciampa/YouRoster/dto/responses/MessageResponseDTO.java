package giuliaciampa.YouRoster.dto.responses;

import java.time.LocalDateTime;

public record MessageResponseDTO(
        String message,
        LocalDateTime timestamp
) {
}
