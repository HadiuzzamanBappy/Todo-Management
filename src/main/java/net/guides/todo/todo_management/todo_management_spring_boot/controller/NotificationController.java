package net.guides.todo.todo_management.todo_management_spring_boot.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.guides.todo.todo_management.todo_management_spring_boot.model.Notifications;
import net.guides.todo.todo_management.todo_management_spring_boot.model.Users;
import net.guides.todo.todo_management.todo_management_spring_boot.service.NotificationService;
import net.guides.todo.todo_management.todo_management_spring_boot.service.UserService;

/**
 * The `NotificationController` class in Java handles notifications, including viewing, broadcasting,
 * marking as read, and deleting notifications.
 */
@Controller
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    private Users getLoggedInUser(ModelMap modelMap) {
        Object princiObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (princiObject instanceof UserDetails) {
            String username = ((UserDetails) princiObject).getUsername();
            return userService.findUserByUsername(username);
        }

        return null;
    }

    @GetMapping("/view")
    public String viewNotifications(@RequestParam(required = false) Notifications.NotificationStatus status,
            ModelMap modelMap) {
        String name = getLoggedInUser(modelMap).getUsername();
        Users loggedInUser = userService.findUserByUsername(name);

        List<Users> user = userService.findAllUsers();
        user.removeIf(userr -> userr.getRole().equals("ADMIN"));

        List<Notifications> notifications;

        Boolean flag;

        if (status == null) {
            notifications = loggedInUser.getRole().equals("ADMIN")
                    ? notificationService.getAllNotificationsForAdmin()
                    : notificationService.getNotificationsForUser(loggedInUser.getId());
            flag = null;
        } else {
            notifications = loggedInUser.getRole().equals("ADMIN")
                    ? notificationService.getNotificationsByStatusForAdmin(status)
                    : notificationService.getNotificationsByStatusForUser(loggedInUser.getId(), status);
            if(status.equals(Notifications.NotificationStatus.READ)){
                flag = true;
            }else{
                flag = false;
            }
        }

        modelMap.addAttribute("notifications", notifications);
        modelMap.addAttribute("notification", new Notifications());
        modelMap.addAttribute("notificationTypes", Arrays.asList(Notifications.NotificationType.values()));
        modelMap.addAttribute("users", user);
        modelMap.addAttribute("filterStatus", flag);
        modelMap.put("page_header", "Todo | Notifications");
        return "notifications";
    }

    @PostMapping("/broadcast")
    public String broadcastNotification(ModelMap modelMap, Notifications notifications) {
        if (notifications.getUser() == null) {
            notificationService.broadcastNotificationToAll(notifications);
        } else {
            notificationService.broadcastNotification(notifications);
        }
        return "redirect:/notifications/view";
    }

    @GetMapping("/mark-as-read")
    public String markAsRead(@RequestParam long id) {
        notificationService.markAsRead(id);
        return "redirect:/notifications/view";
    }

    @GetMapping("/delete")
    public String deleteNotification(@RequestParam long id) {
        notificationService.deleteNotification(id);
        return "redirect:/notifications/view";
    }
}
