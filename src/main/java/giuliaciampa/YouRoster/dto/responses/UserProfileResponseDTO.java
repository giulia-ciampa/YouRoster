package giuliaciampa.YouRoster.dto.responses;

import giuliaciampa.YouRoster.entities.DocumentType;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record UserProfileResponseDTO(
        UUID userId,
        String name,
        String surname,
        LocalDate dateOfBirth,
        String placeOfBirth,
        String phoneNumber,
        String taxCode,
        String photoUrl,
        String streetAddress,
        String houseNumber,
        String zipCode,
        String city,
        String province,
        String iban,
        String documentNumber,
        DocumentType documentType,
        LocalDate issueDate,
        LocalDate expirationDate,
        String documentFrontUrl,
        String documentBackUrl,
        String taxCodeCardFrontUrl,
        String taxCodeCardBackUrl,
        String officeName,
        Set<String> roleNames
) {
}
