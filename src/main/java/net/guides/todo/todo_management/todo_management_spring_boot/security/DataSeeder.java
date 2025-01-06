package net.guides.todo.todo_management.todo_management_spring_boot.security;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import net.guides.todo.todo_management.todo_management_spring_boot.model.Notifications;
import net.guides.todo.todo_management.todo_management_spring_boot.model.Todo_Attachment;
import net.guides.todo.todo_management.todo_management_spring_boot.model.Todo_Category;
import net.guides.todo.todo_management.todo_management_spring_boot.model.Todo_Tag;
import net.guides.todo.todo_management.todo_management_spring_boot.model.Todos;
import net.guides.todo.todo_management.todo_management_spring_boot.model.Users;
import net.guides.todo.todo_management.todo_management_spring_boot.repository.AttachmentRepository;
import net.guides.todo.todo_management.todo_management_spring_boot.repository.CategoryRepository;
import net.guides.todo.todo_management.todo_management_spring_boot.repository.NotificationRepository;
import net.guides.todo.todo_management.todo_management_spring_boot.repository.TagRepository;
import net.guides.todo.todo_management.todo_management_spring_boot.repository.TodoRepository;
import net.guides.todo.todo_management.todo_management_spring_boot.repository.UserRepository;

/**
 * The `DataSeeder` class in Java is responsible for seeding initial data into various repositories
 * like users, categories, tags, attachments, todos, and notifications when the application starts.
 */
@Component
public class DataSeeder implements CommandLineRunner {
        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private CategoryRepository categoryRepository;

        @Autowired
        private TagRepository tagRepository;

        @Autowired
        private AttachmentRepository attachmentRepository;

        @Autowired
        private TodoRepository todoRepository;

        @Autowired
        private NotificationRepository notificationRepository;

        @Override
        @Transactional
        public void run(String... args) throws Exception {
                seedUsers();
                seedCategories();
                seedTags();
                seedAttachment();
                seedTodos();
                seedNotifications();
        }

        private void seedUsers() {
                String adminUsername = "admin";
                if (userRepository.findByUsername(adminUsername) == null) {
                        Users admin = new Users();
                        admin.setName("Administrator");
                        admin.setUsername(adminUsername);
                        admin.setEmail("admin@todo.com");
                        admin.setPassword(passwordEncoder.encode("123456"));
                        admin.setRole("ADMIN");
                        admin.setEnabled(true);
                        admin.setLastLoggedIn(LocalDateTime.now());

                        userRepository.save(admin);
                }

                if (userRepository.count() == 1) {
                        List<Users> users = Arrays.asList(
                                        new Users("John Doe", "john_doe", "john.doe@todo.com", "1234567890",
                                                        passwordEncoder.encode("123456"), "USER", true,
                                                        LocalDateTime.now()),
                                        new Users("Jane Smith", "jane_smith", "jane.smith@todo.com", "2345678901",
                                                        passwordEncoder.encode("123456"), "USER", false,
                                                        LocalDateTime.now()),
                                        new Users("Mike Jones", "mike_jones", "mike.jones@todo.com", "3456789012",
                                                        passwordEncoder.encode("123456"), "USER", true,
                                                        LocalDateTime.now()),
                                        new Users("Emily Davis", "emily_davis", "emily.davis@todo.com",
                                                        "4567890123",
                                                        passwordEncoder.encode("123456"), "ADMIN", false,
                                                        LocalDateTime.now()),
                                        new Users("David Wilson", "david_wilson", "david.wilson@todo.com",
                                                        "5678901234",
                                                        passwordEncoder.encode("123456"), "USER", true,
                                                        LocalDateTime.now()),
                                        new Users("Lucy Brown", "lucy_brown", "lucy.brown@todo.com", "6789012345",
                                                        passwordEncoder.encode("123456"), "USER", true,
                                                        LocalDateTime.now()),
                                        new Users("Paul White", "paul_white", "paul.white@todo.com", "7890123456",
                                                        passwordEncoder.encode("123456"), "USER", true,
                                                        LocalDateTime.now()),
                                        new Users("Susan Green", "susan_green", "susan.green@todo.com",
                                                        "8901234567",
                                                        passwordEncoder.encode("123456"), "EDITOR", true,
                                                        LocalDateTime.now()));

                        userRepository.saveAll(users);
                        System.out.println("Users seeded.");
                }
        }

        private void seedCategories() {
                if (categoryRepository.count() == 0) {
                        List<Todo_Category> categories = Arrays.asList(
                                        new Todo_Category("Work"),
                                        new Todo_Category("Personal"),
                                        new Todo_Category("Daily"),
                                        new Todo_Category("Health"),
                                        new Todo_Category("Study"),
                                        new Todo_Category("Urgent"),
                                        new Todo_Category("Home"),
                                        new Todo_Category("Shopping"));
                        categoryRepository.saveAll(categories);
                        System.out.println("Categories seeded.");
                }
        }

        private void seedTags() {
                if (tagRepository.count() == 0) {
                        List<Todo_Tag> tags = Arrays.asList(
                                        new Todo_Tag("Urgent", LocalDateTime.now()),
                                        new Todo_Tag("Important", LocalDateTime.now()),
                                        new Todo_Tag("High Priority", LocalDateTime.now()),
                                        new Todo_Tag("Low Priority", LocalDateTime.now()),
                                        new Todo_Tag("Completed", LocalDateTime.now()),
                                        new Todo_Tag("Pending", LocalDateTime.now()),
                                        new Todo_Tag("Follow Up", LocalDateTime.now()),
                                        new Todo_Tag("Reminder", LocalDateTime.now()));
                        tagRepository.saveAll(tags);
                        System.out.println("Tags seeded.");
                }
        }

        private void seedAttachment(){
                if (attachmentRepository.count() == 0) {
                        List<Todo_Attachment> attachment = Arrays.asList(
                                new Todo_Attachment("instructions.pdf","instructions.pdf")
                        );
                        attachmentRepository.saveAll(attachment);
                        System.out.println("Tags seeded.");
                }
        }

        private void seedTodos() {
                if (todoRepository.count() == 0) {
                        // Fetch Users by username
                        Users user1 = userRepository.findByUsername("john_doe");
                        Users user2 = userRepository.findByUsername("jane_smith");
                        Users user3 = userRepository.findByUsername("mike_jones");
                        Users user4 = userRepository.findByUsername("emily_davis");
                        Users user5 = userRepository.findByUsername("david_wilson");
                        Users user6 = userRepository.findByUsername("lucy_brown");
                        Users user7 = userRepository.findByUsername("paul_white");
                        Users user8 = userRepository.findByUsername("susan_green");

                        // Fetch Categories by name
                        Todo_Category workCategory = categoryRepository.findByName("Work").orElse(null);
                        Todo_Category personalCategory = categoryRepository.findByName("Personal").orElse(null);
                        Todo_Category dailyCategory = categoryRepository.findByName("Daily").orElse(null);
                        Todo_Category healthCategory = categoryRepository.findByName("Health").orElse(null);
                        Todo_Category studyCategory = categoryRepository.findByName("Study").orElse(null);
                        Todo_Category urgentCategory = categoryRepository.findByName("Urgent").orElse(null);
                        Todo_Category homeCategory = categoryRepository.findByName("Home").orElse(null);
                        Todo_Category shoppingCategory = categoryRepository.findByName("Shopping").orElse(null);

                        // Fetch Tags by name
                        Todo_Tag urgentTag = tagRepository.findByName("Urgent").orElse(null);
                        Todo_Tag importantTag = tagRepository.findByName("Important").orElse(null);
                        Todo_Tag highPriorityTag = tagRepository.findByName("High Priority").orElse(null);
                        Todo_Tag lowPriorityTag = tagRepository.findByName("Low Priority").orElse(null);
                        Todo_Tag completedTag = tagRepository.findByName("Completed").orElse(null);
                        Todo_Tag pendingTag = tagRepository.findByName("Pending").orElse(null);
                        Todo_Tag followUpTag = tagRepository.findByName("Follow Up").orElse(null);
                        Todo_Tag reminderTag = tagRepository.findByName("Reminder").orElse(null);

                        Todo_Attachment instructions = attachmentRepository.findByFileName("instructions.pdf").orElse(null);

                        // Create Todos
                        List<Todos> todos = Arrays.asList(
                                        new Todos(user1.getUsername(), 
                                                        "Work Task One", "Complete the work task",
                                                        Todos.Priority.HIGH,
                                                        Todos.Status.PENDING, 
                                                        workCategory, 
                                                        LocalDateTime.of(2024, 12, 24, 10, 0, 0),
                                                        "Daily", "5", 
                                                        LocalDateTime.of(2024, 12, 24, 10, 0, 0),
                                                        LocalDateTime.of(2024, 12, 24, 10, 0, 0), 
                                                        null,
                                                        Arrays.asList(urgentTag, highPriorityTag, lowPriorityTag),
                                                        null),
                                        new Todos(user2.getUsername(), "Personal Task One", "Complete personal errands",
                                                        Todos.Priority.MEDIUM,
                                                        Todos.Status.PENDING, personalCategory,
                                                        LocalDateTime.of(2024, 12, 24, 10, 0, 0),
                                                        "Weekly", "10", LocalDateTime.of(2024, 12, 24, 10, 10, 0),
                                                        LocalDateTime.of(2024, 12, 24, 10, 10, 0), null,
                                                        Arrays.asList(importantTag, completedTag, pendingTag), instructions),
                                        new Todos(user3.getUsername(), "Health Task", "Gym workout and health checkup",
                                                        Todos.Priority.LOW,
                                                        Todos.Status.COMPLETED, healthCategory,
                                                        LocalDateTime.of(2024, 12, 24, 10, 0, 0),
                                                        "Weekly", "15", LocalDateTime.of(2024, 12, 24, 10, 20, 0),
                                                        LocalDateTime.of(2024, 12, 24, 10, 20, 0),
                                                        LocalDateTime.of(2024, 12, 24, 10, 20, 0),
                                                        Arrays.asList(completedTag, followUpTag), null),
                                        new Todos(user4.getUsername(), "Study Task", "Finish studying for exams",
                                                        Todos.Priority.HIGH,
                                                        Todos.Status.PENDING, studyCategory, LocalDateTime.of(2024, 12, 24, 10, 0, 0),
                                                        "Monthly", "20", LocalDateTime.of(2024, 12, 24, 10, 30, 0),
                                                        LocalDateTime.of(2024, 12, 24, 10, 30, 0), null,
                                                        Arrays.asList(completedTag, pendingTag), null),
                                        new Todos(user5.getUsername(), "Urgent Task", "Complete the urgent task today",
                                                        Todos.Priority.HIGH,
                                                        Todos.Status.PENDING, urgentCategory,
                                                        LocalDateTime.of(2024, 12, 24, 10, 0, 0),
                                                        "Daily", "25", LocalDateTime.of(2024, 12, 24, 10, 40, 0),
                                                        LocalDateTime.of(2024, 12, 24, 10, 40, 0), null,
                                                        Arrays.asList(urgentTag, importantTag), null),
                                        new Todos(user6.getUsername(), "Home Task", "Organize the house",
                                                        Todos.Priority.MEDIUM,
                                                        Todos.Status.IN_PROGRESS, homeCategory,
                                                        LocalDateTime.of(2024, 12, 24, 10, 0, 0),
                                                        "Daily", "30", LocalDateTime.of(2024, 12, 24, 10, 50, 0),
                                                        LocalDateTime.of(2024, 12, 24, 10, 50, 0), null,
                                                        Arrays.asList(followUpTag, pendingTag), null),
                                        new Todos(user7.getUsername(), "Shopping Task", "Go for shopping",
                                                        Todos.Priority.LOW,
                                                        Todos.Status.PENDING, shoppingCategory,
                                                        LocalDateTime.of(2024, 12, 24, 10, 0, 0),
                                                        "Weekly", "35", LocalDateTime.of(2024, 12, 24, 11, 0, 0),
                                                        LocalDateTime.of(2024, 12, 24, 11, 0, 0), null,
                                                        Arrays.asList(highPriorityTag, lowPriorityTag), null),
                                        new Todos(user8.getUsername(), "Health Task Two",
                                                        "Go for a morning run and exercise", Todos.Priority.MEDIUM,
                                                        Todos.Status.PENDING, dailyCategory,
                                                        LocalDateTime.of(2024, 12, 24, 10, 0, 0),
                                                        "Monthly", "40", LocalDateTime.of(2024, 12, 24, 11, 10, 0),
                                                        LocalDateTime.of(2024, 12, 24, 11, 10, 0), null,
                                                        Arrays.asList(completedTag, pendingTag, reminderTag), null));

                        todoRepository.saveAll(todos);
                        System.out.println("Todos seeded.");
                }
        }

        private void seedNotifications() {
                if (notificationRepository.count() == 0) {
                        // Fetch Users by username
                        Users user1 = userRepository.findByUsername("john_doe");
                        Users user2 = userRepository.findByUsername("jane_smith");
                        Users user3 = userRepository.findByUsername("mike_jones");
                        Users user4 = userRepository.findByUsername("emily_davis");
                        Users user5 = userRepository.findByUsername("david_wilson");

                        List<Notifications> notifications = Arrays.asList(
                                        new Notifications("Task deadline is approaching, please complete it soon.",
                                                        Notifications.NotificationType.REMINDER, user1,
                                                        Notifications.NotificationStatus.UNREAD, LocalDateTime.now()),
                                        new Notifications(
                                                        "New broadcast message from the admin: System maintenance tonight.",
                                                        Notifications.NotificationType.ADMIN_BROADCAST, null,
                                                        Notifications.NotificationStatus.UNREAD, LocalDateTime.now()),
                                        new Notifications(
                                                        "Your task \"Finish report\" has been updated to in-progress.",
                                                        Notifications.NotificationType.TASK_UPDATE, user1,
                                                        Notifications.NotificationStatus.UNREAD, LocalDateTime.now()),
                                        new Notifications("Reminder: Your meeting with the client is tomorrow at 3 PM.",
                                                        Notifications.NotificationType.REMINDER, user2,
                                                        Notifications.NotificationStatus.UNREAD, LocalDateTime.now()),
                                        new Notifications(
                                                        "A new comment was added to your task \"Prepare presentation\".",
                                                        Notifications.NotificationType.TASK_UPDATE, user3,
                                                        Notifications.NotificationStatus.UNREAD, LocalDateTime.now()),
                                        new Notifications("Your profile has been successfully updated.",
                                                        Notifications.NotificationType.SYSTEM, user4,
                                                        Notifications.NotificationStatus.READ, LocalDateTime.now()),
                                        new Notifications(
                                                        "Reminder: You have an appointment with the doctor at 10 AM tomorrow.",
                                                        Notifications.NotificationType.REMINDER, user5,
                                                        Notifications.NotificationStatus.UNREAD, LocalDateTime.now()),
                                        new Notifications(
                                                        "System maintenance is scheduled for tonight, please save your work.",
                                                        Notifications.NotificationType.SYSTEM, null,
                                                        Notifications.NotificationStatus.READ, LocalDateTime.now()));

                        notificationRepository.saveAll(notifications);
                        System.out.println("Notifications seeded.");
                }
        }
}
