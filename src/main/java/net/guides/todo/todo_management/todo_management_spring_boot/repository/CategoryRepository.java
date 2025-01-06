package net.guides.todo.todo_management.todo_management_spring_boot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.todo.todo_management.todo_management_spring_boot.model.Todo_Category;

// This code snippet defines a Spring Data JPA repository interface called `CategoryRepository`. It
// extends the `JpaRepository` interface, specifying the entity type `Todo_Category` and the primary
// key type `Long`.
@Repository
public interface CategoryRepository extends JpaRepository<Todo_Category, Long> {
    Optional<Todo_Category> findByName(String name);
}
