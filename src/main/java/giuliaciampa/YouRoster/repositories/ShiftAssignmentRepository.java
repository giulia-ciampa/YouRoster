package giuliaciampa.YouRoster.repositories;

import giuliaciampa.YouRoster.entities.Shift;
import giuliaciampa.YouRoster.entities.ShiftAssignment;
import giuliaciampa.YouRoster.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShiftAssignmentRepository extends JpaRepository<ShiftAssignment, UUID> {

    boolean existsByUserAndShiftDate(User user, LocalDate shiftDate);

    Page<ShiftAssignment> findByShiftDate(LocalDate shiftDate, Pageable pageable);

    Page<ShiftAssignment> findByShiftDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<ShiftAssignment> findByUserAndShiftDateBetween(User user, LocalDate startDate, LocalDate endDate, Pageable pageable);

    Optional<ShiftAssignment> findByUserAndShiftDate(User user, LocalDate shiftDate);

    List<ShiftAssignment> findByShiftAndShiftDateAndUserNot(Shift shift, LocalDate shiftDate, User user);


    @Query("SELECT sa FROM ShiftAssignment sa " +
            "WHERE sa.shiftDate = :shiftDate " +
            "AND sa.user != :user " +
            "AND sa.shift IS NOT NULL " +
            "AND sa.shift.office.id = :officeId " +
            "AND sa.shift.startTime < :myEndTime " +
            "AND sa.shift.endTime > :myStartTime")
    List<ShiftAssignment> findOverlappingColleagues(
            @Param("shiftDate") LocalDate shiftDate,
            @Param("user") User user,
            @Param("officeId") UUID officeId,
            @Param("myStartTime") LocalTime myStartTime,
            @Param("myEndTime") LocalTime myEndTime
    );
}
