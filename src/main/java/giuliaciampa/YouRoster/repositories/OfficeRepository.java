package giuliaciampa.YouRoster.repositories;

import giuliaciampa.YouRoster.entities.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OfficeRepository extends JpaRepository<Office, UUID> {
    boolean existsByName(String name);
}
