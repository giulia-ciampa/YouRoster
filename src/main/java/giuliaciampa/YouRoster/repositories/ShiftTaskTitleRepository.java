package giuliaciampa.YouRoster.repositories;

import giuliaciampa.YouRoster.entities.ShiftTaskTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShiftTaskTitleRepository extends JpaRepository<ShiftTaskTitle, UUID> {

    boolean existsByTitle(String title);
}
