package net.guides.todo.todo_management.todo_management_spring_boot.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.guides.todo.todo_management.todo_management_spring_boot.model.Users;
import net.guides.todo.todo_management.todo_management_spring_boot.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // WRONG: The parameter is 'id', not 'userid'
    // @Cacheable(value = "users", key = "#userid")
    // CORRECT: The key now matches the parameter name 'id'
    @Cacheable(value = "users", key = "#id")
    public Users findUserById(long id) {
        return userRepository.findById(id);
    }

    // WRONG: The parameter is 'email', not 'useremail'
    // @Cacheable(value = "users", key = "#useremail")
    // CORRECT: The key now matches the parameter name 'email'
    @Cacheable(value = "users", key = "#email")
    public Users findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // This one was already correct, no change needed.
    @Cacheable(value = "users", key = "#username")
    public Users findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // WRONG: No parameter named 'userall' exists in this method
    // @Cacheable(value = "users", key = "#userall")
    // CORRECT: Use a static string key (in single quotes) when there are no parameters
    @Cacheable(value = "users", key = "'allUsers'")
    public List<Users> findAllUsers() {
        return userRepository.findAll();
    }

    // WRONG: The parameter is 'pageable', not 'userpageable'
    // @Cacheable(value = "users", key = "#userpageable")
    // CORRECT: The key now references the page number and size for uniqueness
    @Cacheable(value = "users", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<Users> findPageableAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    // WRONG: No parameter named 'userstatus' exists
    // @Cacheable(value = "users", key = "#userstatus")
    // CORRECT: The key now combines page info and the active status for a unique key
    @Cacheable(value = "users", key = "#pageable.pageNumber + '-' + #active")
    public Page<Users> filterUsersByStatus(Pageable pageable, boolean active) {
        return userRepository.findByEnabled(pageable, active);
    }

    public boolean saveUser(Users user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return false;
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return false;
        }
        user.setLastLoggedIn(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public void updateUser(Users user) {
        userRepository.save(user);
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    public long getActiveUserCount() {
        return userRepository.countByEnabled(true);
    }

    public List<Users> getLatestUsers() {
        LocalDateTime tenDaysAgo = LocalDateTime.now().minusDays(10);
        return userRepository.findUsersCreatedWithin(tenDaysAgo);
    }
}