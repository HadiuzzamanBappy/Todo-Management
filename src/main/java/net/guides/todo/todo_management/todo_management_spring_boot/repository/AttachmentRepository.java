package net.guides.todo.todo_management.todo_management_spring_boot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.todo.todo_management.todo_management_spring_boot.model.Todo_Attachment;

// This code snippet is defining a repository interface in a Spring Boot application.
@Repository
public interface AttachmentRepository extends JpaRepository<Todo_Attachment, Long> {
    Optional<Todo_Attachment> findByFileName(String fileName);
}
