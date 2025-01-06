package net.guides.todo.todo_management.todo_management_spring_boot.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

/**
 * The `Notifications` class represents notifications with message, type, user association, status, and
 * creation timestamp in a Java application.
 */
@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message", nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private Users user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus status = NotificationStatus.UNREAD;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public enum NotificationType {
        REMINDER,
        ADMIN_BROADCAST,
        SYSTEM,
        TASK_UPDATE,
        OTHER
    }

    public enum NotificationStatus {
        READ,
        UNREAD
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Notifications(String message, NotificationType type, Users user, NotificationStatus status, LocalDateTime createdAt) {
        this.message = message;
        this.type = type;
        this.user = user;
        this.status = status;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }
    
}
