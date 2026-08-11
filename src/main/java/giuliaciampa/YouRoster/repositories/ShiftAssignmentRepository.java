package giuliaciampa.YouRoster.repositories;

import giuliaciampa.YouRoster.entities.Shift;
import giuliaciampa.YouRoster.entities.ShiftAssignment;
import giuliaciampa.YouRoster.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShiftAssignmentRepository extends JpaRepository<ShiftAssignment, UUID> {

    boolean existsByUserAndShiftDate(User user, LocalDate shiftDate);

    Page<ShiftAssignment> findByShiftDate(LocalDate shiftDate, Pageable pageable);

    Page<ShiftAssignment> findByShiftDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<ShiftAssignment> findByUserAndShiftDateBetween(User user, LocalDate startDate, LocalDate endDate, Pageable pageable);

    Optional<ShiftAssignment> findByUserAndShiftDate(User user, LocalDate shiftDate);

    List<ShiftAssignment> findByShiftAndShiftDateAndUserNot(Shift shift, LocalDate shiftDate, User user);

}
