package giuliaciampa.YouRoster.dto.responses;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserRegistrationResponseDTO(
        UUID id,
        String name,
        String surname,
        String email,
        String message,
        LocalDateTime time) {
}
