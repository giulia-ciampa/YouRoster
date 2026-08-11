package giuliaciampa.YouRoster.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "task_types")
public class TaskType {

    //ATTRIBUTI
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String title;

    private String description;

    //COSTRUTTORE
    public TaskType(String title, String description) {
        this.title = title;
        this.description = description;
    }

    //COSTRUTTORE VUOTO
    public TaskType() {
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

    //TO STRING

    @Override
    public String toString() {
        return "TaskType{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                '}';
    }
}
