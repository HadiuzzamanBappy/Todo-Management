package net.guides.todo.todo_management.todo_management_spring_boot.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

/**
 * The `Todo_Tag` class represents a tag entity with properties like id, name, created date, and a
 * many-to-many relationship with Todos.
 */
@Data
@Entity
@Table(name = "tags")
@NoArgsConstructor
@AllArgsConstructor
public class Todo_Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created_date", nullable = true)
    private LocalDateTime createdDate;

    // To map to todos table (Many-to-Many)
    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private List<Todos> todos = new ArrayList<>();

    public Todo_Tag(String name, LocalDateTime createdDate) {
        this.name = name;
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
