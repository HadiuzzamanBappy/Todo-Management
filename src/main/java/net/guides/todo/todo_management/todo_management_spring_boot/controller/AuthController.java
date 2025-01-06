package net.guides.todo.todo_management.todo_management_spring_boot.controller;

import java.text.ParseException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.guides.todo.todo_management.todo_management_spring_boot.model.Notifications;
import net.guides.todo.todo_management.todo_management_spring_boot.model.Users;
import net.guides.todo.todo_management.todo_management_spring_boot.service.NotificationService;
import net.guides.todo.todo_management.todo_management_spring_boot.service.TodoService;
import net.guides.todo.todo_management.todo_management_spring_boot.service.UserService;

/**
 * The AuthController class in Java handles user authentication, registration, and dashboard
 * functionalities for a Todo application.
 */
@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private TodoService todoService;

    @Autowired
    private NotificationService notificationService;

    private Users getLoggedInUser(ModelMap modelMap) {
        Object princiObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (princiObject instanceof UserDetails) {
            String username = ((UserDetails) princiObject).getUsername();
            return userService.findUserByUsername(username);
        }

        return null;
    }

    @GetMapping("/login")
    public String loginPage(ModelMap modelMap) {
        modelMap.put("page_header", "Todo | Login");
        return "auth";
    }

    @GetMapping("/register")
    public String showRegisterUserPage(ModelMap modelMap) {
        modelMap.put("page_header", "Todo | Login");
        modelMap.put("user", new Users());
        return "auth";
    }

    @PostMapping("/register")
    public String registerUser(ModelMap modelMap, Users users) throws ParseException {
        boolean success = userService.saveUser(users);
        if (!success) {
            modelMap.put("error", "Username or email already exists");
            return "auth";
        }else{
            modelMap.put("error", "Registration completed.Please login now");
            return "auth";
        }        
    }

    @GetMapping("/")
    public String showWelcomePage(ModelMap model) {
        String name = getLoggedInUser(model).getUsername();
        Users loggedInUser = userService.findUserByUsername(name);
        
        List<Users> allUsers = userService.findAllUsers();

        List<Notifications> notifications = loggedInUser.getRole().equals("ADMIN")
                ? notificationService.getAllNotificationsForAdmin()
                : notificationService.getNotificationsForUser(loggedInUser.getId());
        List<Notifications> latestNotifications = notifications.stream()
                .sorted(Comparator.comparing(Notifications::getCreatedAt).reversed())
                .limit(4)
                .collect(Collectors.toList());

        model.put("totalUsers", allUsers.size());
        model.put("activeUsers", userService.getActiveUserCount());
        model.put("inactiveUsers", allUsers.size() - userService.getActiveUserCount());
        model.put("latestUsers", userService.getLatestUsers().size());

        model.put("totalTodos", todoService.getTotalTodoCount());
        model.put("completedTodos", todoService.getCompletedTodoCount());
        model.put("pendingTodos", todoService.getPendingTodoCount());
        model.put("ongoingTodos", todoService.getOngoingTodoCount());
        model.put("overdueTodos", todoService.getOverdueTodoCount());

        model.put("latestNotifications", latestNotifications);
        model.put("page_header", "Todo | Home");
        return "dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, authentication);
        }

        return "redirect:/";
    }
}
