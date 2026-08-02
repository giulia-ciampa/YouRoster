package giuliaciampa.YouRoster.dto.requests;

import giuliaciampa.YouRoster.entities.Role;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record AdminApprovalRequestDTO(
        @NotEmpty(message = "Bisogna assegnare almeno un ruolo")
        Set<Role> roles
) {
}
