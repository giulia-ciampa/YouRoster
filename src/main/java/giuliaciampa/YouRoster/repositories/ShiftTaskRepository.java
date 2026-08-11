package giuliaciampa.YouRoster.repositories;

import giuliaciampa.YouRoster.entities.ShiftTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShiftTaskRepository extends JpaRepository<ShiftTask, UUID> {

}
