package net.guides.todo.todo_management.todo_management_spring_boot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.guides.todo.todo_management.todo_management_spring_boot.model.Notifications;

// This Java code snippet defines a repository interface named `NotificationRepository` that extends
// `JpaRepository` interface for managing entities of type `Notifications` with primary key of type
// `Long`. Here's a breakdown of the methods defined in this interface:
@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Long> {
    List<Notifications> findByUserId(Long userId);

    @Query("SELECT n FROM Notifications n WHERE n.status = :status")
    List<Notifications> findByStatus(@Param("status") Notifications.NotificationStatus status);

    @Query("SELECT n FROM Notifications n WHERE n.user.id = :userId AND n.status = :status")
    List<Notifications> findByUserIdAndStatus(@Param("userId") Long userId,
            @Param("status") Notifications.NotificationStatus status);

}
