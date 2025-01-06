package net.guides.todo.todo_management.todo_management_spring_boot.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Size;

import jakarta.persistence.*;
import lombok.*;

/**
 * The Todos class represents a task entity with various properties such as title, description,
 * priority, status, due time, tags, and attachments.
 */
@Entity
@Table(name = "todos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Todos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String userName;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = true, length = 1000)
    @Size(min = 5, message = "Enter at least 10 characters...")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority = Priority.LOW;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.PENDING;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Todo_Category category;

    @Column(name = "due_time", nullable = true)
    private LocalDateTime dueTime;

    @ManyToMany
    @JoinTable(name = "todo_tags", joinColumns = @JoinColumn(name = "todo_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Todo_Tag> tags;

    @Column(name = "recurrence", nullable = true)
    private String recurrence;

    @Column(name = "reminder", nullable = true)
    private String reminder;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    @Column(name = "completion_date", nullable = true)
    private LocalDateTime completionDate;

    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Todo_Attachment> attachments;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }

    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    public enum Status {
        PENDING, COMPLETED, IN_PROGRESS
    }

    @SuppressWarnings("unchecked")
    public Todos(String username2, String title, String description, Priority priority, Status status,
            Todo_Category category, LocalDateTime dueTime, String recurrence, String reminder,
            LocalDateTime createdDate, LocalDateTime lastUpdated, Object completionDate, List<Todo_Tag> tags,
            Object attachments) {
        this.userName = username2;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.category = category;
        this.dueTime = dueTime;
        this.recurrence = recurrence;
        this.reminder = reminder;
        this.createdDate = createdDate;
        this.lastUpdated = lastUpdated;
        this.completionDate = (completionDate != null) ? (LocalDateTime) completionDate : null;
        this.tags = tags;
        this.attachments = (attachments != null) ? (List<Todo_Attachment>) attachments : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Todos{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority='" + priority + '\'' +
                ", status='" + status + '\'' +
                ", Category='" + category + '\'' +
                ", dueTime=" + dueTime +
                ", tags=" + tags +
                ", recurrence='" + recurrence + '\'' +
                ", reminder=" + reminder +
                ", lastUpdated=" + lastUpdated +
                ", completionDate=" + completionDate +
                ", attachments=" + attachments +
                '}';
    }

}
