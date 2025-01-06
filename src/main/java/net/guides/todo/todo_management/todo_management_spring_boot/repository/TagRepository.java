package net.guides.todo.todo_management.todo_management_spring_boot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.todo.todo_management.todo_management_spring_boot.model.Todo_Tag;

// This code snippet is defining a repository interface in a Spring Boot application for managing
// `Todo_Tag` entities. Here's what each part of the code does:
@Repository
public interface TagRepository extends JpaRepository<Todo_Tag, Long> {
    Optional<Todo_Tag> findByName(String name);
}
