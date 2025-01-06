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

/**
 * The `UserService` class in Java implements methods for user management such as finding users by ID,
 * email, or username, saving, updating, and deleting users, as well as retrieving user counts and
 * filtering users by status.
 */
@Service
public class UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Cacheable(value = "users", key = "#userid")
    public Users findUserById(long id) {
        return userRepository.findById(id);
    }

    @Cacheable(value = "users", key = "#useremail")
    public Users findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Cacheable(value = "users", key = "#username")
    public Users findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Cacheable(value = "users", key = "#userall")
    public List<Users> findAllUsers() {
        List<Users> users = userRepository.findAll();

        return users;
    }

    @Cacheable(value = "users", key = "#userpageable")
    public Page<Users> findPageableAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Cacheable(value = "users", key = "#userstatus")
    public Page<Users> filterUsersByStatus(Pageable pageable,boolean active) {
        return userRepository.findByEnabled(pageable,active);
    }

    public boolean saveUser(Users user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return false;
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return false;
        }

        try {
            user.setLastLoggedIn(LocalDateTime.now());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
