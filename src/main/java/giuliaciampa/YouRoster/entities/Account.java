package giuliaciampa.YouRoster.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class Account {

    //ATTRIBUTI
    @Id
    @GeneratedValue
    @Column(name = "account_id")
    private UUID id;

    @Column(length = 50, unique = true, nullable = false)
    private String email;

    @JsonIgnore
    @Column(length = 100, nullable = false)
    private String password;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
            name = "accounts_roles",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    //COSTRUTTORE
    public Account(String email, String password) {
        this.email = email;
        this.password = password;
        this.isActive = false;
    }

    //COSTRUTTORE VUOTO

    public Account() {
    }

    //METODO ATTIVAZIONE ACCOUNT

    public void activate() {
        isActive = true;
    }

    //METODO DISATTIVAZIONE ACCOUNT
    public void deactivate() {
        isActive = false;
    }

    //GETTER E SETTER

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return isActive;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    //TO STRING

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", email=" + email + '\'' +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}
