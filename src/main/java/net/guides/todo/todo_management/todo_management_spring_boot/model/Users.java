package net.guides.todo.todo_management.todo_management_spring_boot.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Users class represents a user entity with various attributes and JPA annotations for database
 * mapping.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone", nullable = true)
    private String phone;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role = "USER";

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @Column(name = "lastLoggedIn", nullable = false)
    private LocalDateTime lastLoggedIn;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }

    public Users(String name, String username, String email, String phone, String password, String role,
            boolean enabled, LocalDateTime lastLoggedIn) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
        this.lastLoggedIn = lastLoggedIn;
        this.createdDate = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
    }
}
