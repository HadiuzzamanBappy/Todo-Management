package net.guides.todo.todo_management.todo_management_spring_boot.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.guides.todo.todo_management.todo_management_spring_boot.model.Users;

// This code snippet defines a repository interface in a Spring Boot application for managing user
// entities. Here's a breakdown of what each method does:
public interface UserRepository extends JpaRepository<Users, Long>{
    Users findByUsername(String username);
    Users findByEmail(String email);
    Users findById(long id);
    long countByEnabled(boolean enabled);

    @Query("SELECT u FROM Users u WHERE u.createdDate >= :startDate")
    List<Users> findUsersCreatedWithin(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT u FROM Users u WHERE u.enabled = :active")
    Page<Users> findByEnabled(Pageable pageable,@Param("active") boolean active);
}
