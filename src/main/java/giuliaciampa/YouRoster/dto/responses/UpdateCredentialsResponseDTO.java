package giuliaciampa.YouRoster.dto.responses;

import java.time.LocalDateTime;

public record UpdateCredentialsResponseDTO(
        String message,
        LocalDateTime time
) {
}
