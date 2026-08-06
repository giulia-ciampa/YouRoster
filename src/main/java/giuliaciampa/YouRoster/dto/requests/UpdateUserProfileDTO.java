package giuliaciampa.YouRoster.dto.requests;

import giuliaciampa.YouRoster.entities.DocumentType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record UpdateUserProfileDTO(
        String phoneNumber,
        String photoUrl,
        String streetAddress,
        String houseNumber,
        @Pattern(regexp = "^[0-9]{5}$", message = "Il CAP deve essere di 5 cifre")
        String zipCode,
        String iban,
        String documentNumber,
        DocumentType documentType,
        @PastOrPresent(message = "la data di emissione del documento non può essere al futuro")
        LocalDate issueDate,
        @Future(message = "Il documento è scaduto!")
        LocalDate expirationDate,
        String documentFrontUrl,
        String documentBackUrl,
        String taxCodeCardFrontUrl,
        String taxCodeCardBackUrl
) {
}
