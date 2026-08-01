package giuliaciampa.YouRoster.repositories;

import giuliaciampa.YouRoster.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
}
