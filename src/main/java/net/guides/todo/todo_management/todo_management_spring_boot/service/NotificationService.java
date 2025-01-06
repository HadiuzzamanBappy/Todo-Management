package net.guides.todo.todo_management.todo_management_spring_boot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.todo.todo_management.todo_management_spring_boot.model.Notifications;
import net.guides.todo.todo_management.todo_management_spring_boot.model.Users;
import net.guides.todo.todo_management.todo_management_spring_boot.repository.NotificationRepository;
import net.guides.todo.todo_management.todo_management_spring_boot.repository.UserRepository;

/**
 * The `NotificationService` class in Java provides methods for managing notifications, including
 * retrieving, creating, broadcasting, marking as read, and deleting notifications for users and
 * admins.
 */
@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Notifications> getNotificationsForUser(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    public List<Notifications> getAllNotificationsForAdmin() {
        return notificationRepository.findAll();
    }

    public void createNotification(String message, Notifications.NotificationType type, Users user) {
        Notifications notification = new Notifications();
        notification.setMessage(message);
        notification.setType(type);
        notification.setUser(user);
        notification.setStatus(Notifications.NotificationStatus.UNREAD);
        notificationRepository.save(notification);
    }
    
    public void broadcastNotificationToAll(Notifications notification) {
        List<Users> users = userRepository.findAll();
        users.removeIf(userr -> userr.getRole().equals("ADMIN"));

        for (Users user : users) {
            Notifications newNotifications = new Notifications();
            newNotifications.setMessage(notification.getMessage());
            newNotifications.setStatus(notification.getStatus());
            newNotifications.setType(notification.getType());
            newNotifications.setUser(user);
            notificationRepository.save(newNotifications);
        }
    }

    public void broadcastNotification(Notifications notification) {
        notificationRepository.save(notification);
    }

    public void markAsRead(Long notificationId) {
        Notifications notification = notificationRepository.findById(notificationId).orElse(null);
        if (notification != null) {
            notification.setStatus(Notifications.NotificationStatus.READ);
            notificationRepository.save(notification);
        }
    }

    public void deleteNotification(Long notificationId) {
        Notifications notification = notificationRepository.findById(notificationId).orElse(null);        
        if (notification != null) {
            notificationRepository.delete(notification);
        }
    }

    public List<Notifications> getNotificationsByStatusForAdmin(Notifications.NotificationStatus status) {
        return notificationRepository.findByStatus(status);
    }
    
    public List<Notifications> getNotificationsByStatusForUser(Long userId, Notifications.NotificationStatus status) {
        return notificationRepository.findByUserIdAndStatus(userId, status);
    }
}
