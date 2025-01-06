package net.guides.todo.todo_management.todo_management_spring_boot.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.guides.todo.todo_management.todo_management_spring_boot.model.Todos;

// This Java code snippet defines a repository interface `TodoRepository` that extends `JpaRepository`
// for the entity class `Todos`. Here's a breakdown of the methods defined in this interface:
public interface TodoRepository extends JpaRepository<Todos, Long> {

        Page<Todos> findByUserName(String username, Pageable pageable);

        Todos findById(long id);

        long countByStatus(Todos.Status status);

        @Query("SELECT COUNT(t) FROM Todos t WHERE t.dueTime < CURRENT_DATE AND t.status != 'COMPLETED'")
        long countOverdueTodos();

        @Query("SELECT t FROM Todos t WHERE " +
                        "(:priority IS NULL OR t.priority = :priority) AND " +
                        "(:status IS NULL OR t.status = :status) AND " +
                        "(COALESCE(:category, '') = '' OR t.category.name = :category)")
        List<Todos> filterTodos(
                        @Param("priority") Todos.Priority priority,
                        @Param("status") Todos.Status status,
                        @Param("category") String category);
}
