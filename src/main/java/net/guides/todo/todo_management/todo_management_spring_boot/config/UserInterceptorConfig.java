package net.guides.todo.todo_management.todo_management_spring_boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.guides.todo.todo_management.todo_management_spring_boot.model.Users;
import net.guides.todo.todo_management.todo_management_spring_boot.service.UserService;

/**
 * The `UserInterceptorConfig` class in Java is used to set model attributes for the logged-in user in
 * a Spring MVC application based on authentication information.
 */
@Configuration
public class UserInterceptorConfig implements WebMvcConfigurer, HandlerInterceptor {

    @Autowired
    private UserService userService;

    @SuppressWarnings("null")
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this); // Register this class as the interceptor
    }
    
    /**
     * The function sets model attributes for the logged-in user based on authentication information in
     * a Spring MVC application.
     */
    @SuppressWarnings("null")
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();

                if (principal instanceof UserDetails) {
                    UserDetails userDetails = (UserDetails) principal;
                    Users user = userService.findUserByUsername(userDetails.getUsername());

                    modelAndView.addObject("loggedInUserId", user.getId());
                    modelAndView.addObject("loggedInUsername", user.getUsername());
                    modelAndView.addObject("loggedInUserRole", user.getRole());
                }
            }
        }
    }
}

