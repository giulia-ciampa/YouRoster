package giuliaciampa.YouRoster.dto.responses;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record MessageResponseDTO(
        String message,

        @JsonFormat(pattern = "HH:mm")
        LocalDateTime timestamp
) {
}
