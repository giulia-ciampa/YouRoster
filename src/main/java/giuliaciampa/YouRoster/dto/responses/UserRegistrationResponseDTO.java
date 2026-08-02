package giuliaciampa.YouRoster.dto.responses;

import java.time.LocalDateTime;

public record UserRegistrationResponseDTO(String name,
                                          String surname,
                                          String email,
                                          String message,
                                          LocalDateTime time) {
}
