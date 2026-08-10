package giuliaciampa.YouRoster.dto.responses;

import giuliaciampa.YouRoster.entities.OfficeStatus;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

public record OfficeResponseDTO(
        UUID id,
        String name,
        String street,
        String houseNumber,
        String zipCode,
        String city,
        String province,
        LocalTime openingTime,
        LocalTime closingTime,
        OfficeStatus status,
        BigDecimal latitude,
        BigDecimal longitude

) {
}
