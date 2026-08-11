package giuliaciampa.YouRoster.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "shift_task_tiles")
public class ShiftTaskTitle {

    //ATTRIBUTI
    @Id
    @GeneratedValue
    @Column(name = "shift_task_title_id")
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String title;

    @Column(length = 300)
    private String description;

    private boolean isActive = true;

    //COSTRUTTORE
    public ShiftTaskTitle(String title, String description) {
        this.title = title;
        this.description = description;
    }

    //COSTRUTTORE VUOTO
    public ShiftTaskTitle() {
    }

    //GETTER E SETTER
    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    //TO STRING

    @Override
    public String toString() {
        return "ShiftTaskTitle{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", isActive=" + isActive() +
                ", active=" + isActive() +
                '}';
    }
}
