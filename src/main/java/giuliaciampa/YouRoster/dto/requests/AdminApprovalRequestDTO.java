package giuliaciampa.YouRoster.dto.requests;

import jakarta.validation.constraints.NotEmpty;

import java.util.Set;
import java.util.UUID;

public record AdminApprovalRequestDTO(
        @NotEmpty(message = "Bisogna assegnare almeno un ruolo")
        Set<String> roles,

        UUID officeId //null se l'utente non è un coordinator o se non ha una sede assegnata

) {
}
