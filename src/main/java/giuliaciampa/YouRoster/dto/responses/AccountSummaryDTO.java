package giuliaciampa.YouRoster.dto.responses;

import java.util.UUID;

public record AccountSummaryDTO(
        UUID accountId,
        String name,
        String surname,
        String phoneNumber,
        String photoUrl,
        String officeName,
        String email,
        boolean isActive
) {
}
