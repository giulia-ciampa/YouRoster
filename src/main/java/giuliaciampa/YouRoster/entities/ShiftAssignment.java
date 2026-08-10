package giuliaciampa.YouRoster.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "shift_assignments")
public class ShiftAssignment {

    //ATTRIBUTI

    @Id
    @GeneratedValue
    @Column(name = "shift_assignments_id")
    private UUID id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private User user;

    @JoinColumn(name = "shift_id", nullable = false)
    @ManyToOne
    private Shift shift;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


    private String tasks;

    @Column(name = "shift_date", nullable = false)
    private LocalDate shiftDate;

    //COSTRUTTORE
    public ShiftAssignment(User user, Shift shift, String tasks, LocalDate shiftDate) {
        this.user = user;
        this.shift = shift;
        this.createdAt = LocalDateTime.now();
        this.tasks = tasks;
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

    public String getTasks() {
        return tasks;
    }

    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    public LocalDate getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(LocalDate shiftDate) {
        this.shiftDate = shiftDate;
    }

    //TO STRING
    @Override
    public String toString() {
        return "ShiftAssignment{" +
                "id=" + id +
                ", user=" + getUser() +
                ", shift=" + getShift() +
                ", createdAt=" + getCreatedAt() +
                ", tasks='" + getTasks() + '\'' +
                ", shiftDate=" + getShiftDate() +
                '}';
    }
}
