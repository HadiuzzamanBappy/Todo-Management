package net.guides.todo.todo_management.todo_management_spring_boot.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The `Todo_Category` class represents a category entity with an ID, name, and a list of associated
 * all todos, using JPA annotations for persistence.
 */
@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Todo_Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Todos> todos = new ArrayList<>();

    public Todo_Category(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Todo_Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
