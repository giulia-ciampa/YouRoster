package giuliaciampa.YouRoster.entities;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "shifts")
public class Shift {
    //ATTRIBUTI
    @Id
    @GeneratedValue
    @Column(name = "shift_id")
    private UUID id;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "is_rest")
    private boolean isRest;

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "office_id", nullable = false)
    private Office office;

    //COSTRUTTORE
    public Shift(LocalTime startTime, LocalTime endTime, boolean isRest, Office office) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.isRest = isRest;
        this.isActive = true;
        this.office = office;
    }

    //COSTRUTTORE VUOTO
    public Shift() {
    }

    //GETTER E SETTER

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public boolean isRest() {
        return isRest;
    }

    public void setRest(boolean rest) {
        isRest = rest;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    //TO STRING


    @Override
    public String toString() {
        return "Shift{" +
                "id=" + id +
                ", startTime=" + getStartTime() +
                ", endTime=" + getEndTime() +
                ", isRest=" + isRest() +
                ", isActive=" + isActive() +
                ", office=" + getOffice() +
                '}';
    }
}
