package giuliaciampa.YouRoster.repositories;

import giuliaciampa.YouRoster.entities.Office;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OfficeRepository extends JpaRepository<Office, UUID> {
}
