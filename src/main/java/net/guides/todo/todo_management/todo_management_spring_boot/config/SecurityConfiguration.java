package net.guides.todo.todo_management.todo_management_spring_boot.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.guides.todo.todo_management.todo_management_spring_boot.model.Users;
import net.guides.todo.todo_management.todo_management_spring_boot.repository.UserRepository;

/**
 * The `SecurityConfiguration` class in Java Spring sets up password encoding,
 * user details retrieval,
 * security filters, form login, and a custom authentication failure handler for
 * a web application.
 */
@Configuration
public class SecurityConfiguration {
    private final UserRepository userRepository;

    public SecurityConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * The function creates and returns a BCryptPasswordEncoder instance for
     * password encoding in Java.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * This function defines a UserDetailsService bean in Java that retrieves user
     * details from a
     * UserRepository based on the username provided.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Users user = userRepository.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            } else if (!user.isEnabled()) {
                throw new UsernameNotFoundException("User is not active");
            }
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole())
                    .build();
        };
    }

    /**
     * The function configures security filters for different HTTP requests in a
     * Java Spring
     * application, allowing access to specific paths without authentication and
     * requiring roles for
     * others, setting up form login with custom failure handling, and disabling
     * CSRF protection.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/**").hasAnyRole("USER", "ADMIN"))
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureHandler(customAuthenticationFailureHandler())
                        .permitAll()
                        .defaultSuccessUrl("/", true))
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                );

        return httpSecurity.build();
    }

    /**
     * This function creates a custom authentication failure handler in Java Spring
     * that sets an
     * authentication error message in the session and redirects to the login page
     * with an error
     * parameter.
     */
    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                    org.springframework.security.core.AuthenticationException exception)
                    throws IOException, ServletException {
                request.getSession().setAttribute("AUTH_ERROR", exception.getMessage());
                response.sendRedirect("/login?error=true");
            }
        };
    }
}
