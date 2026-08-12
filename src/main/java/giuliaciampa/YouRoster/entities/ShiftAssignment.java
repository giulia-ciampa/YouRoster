package giuliaciampa.YouRoster.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "shift_assignments")
public class ShiftAssignment {

    //ATTRIBUTI

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    @Id
    @GeneratedValue
    @Column(name = "shift_assignments_id")
    private UUID id;
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private User user;
    @JoinColumn(name = "shift_id")
    @ManyToOne
    private Shift shift;
    @Column(name = "shift_date", nullable = false)
    private LocalDate shiftDate;
    @Column(name = "assignment_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AssignmentType assignmentType;


    //COSTRUTTORE
    public ShiftAssignment(User user, Shift shift, LocalDate shiftDate) {
        this.user = user;
        this.shift = shift;
        this.shiftDate = shiftDate;
    }

    //COSTRUTTORE VUOTO
    public ShiftAssignment() {
    }

    //GETTER E SETTER

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDate getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(LocalDate shiftDate) {
        this.shiftDate = shiftDate;
    }

    public AssignmentType getAssignmentType() {
        return assignmentType;
    }

    public void setAssignmentType(AssignmentType assignmentType) {
        this.assignmentType = assignmentType;
    }

    public UUID getId() {
        return id;
    }

    //TO STRING
    @Override
    public String toString() {
        return "ShiftAssignment{" +
                "id=" + id +
                ", user=" + getUser() +
                ", shift=" + getShift() +
                ", createdAt=" + getCreatedAt() +
                ", shiftDate=" + getShiftDate() +
                '}';
    }
}
