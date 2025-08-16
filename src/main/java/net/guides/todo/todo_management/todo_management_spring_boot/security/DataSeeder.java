package net.guides.todo.todo_management.todo_management_spring_boot.security;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
 * The `DataSeeder` class in Java is responsible for seeding initial data into
 * various repositories
 * like users, categories, tags, attachments, todos, and notifications when the
 * application starts.
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

        private void seedTodos() {
                if (todoRepository.count() > 0) {
                        return; // Avoids re-seeding data if it already exists
                }

                // ====================================================================================
                // 1. FETCH ALL PREREQUISITE DATA
                // ====================================================================================

                // Fetch all Users by username
                Users user1 = userRepository.findByUsername("john_doe");
                Users user2 = userRepository.findByUsername("jane_smith");
                Users user3 = userRepository.findByUsername("mike_jones");
                Users user4 = userRepository.findByUsername("emily_davis");
                Users user5 = userRepository.findByUsername("david_wilson");
                Users user6 = userRepository.findByUsername("lucy_brown");
                Users user7 = userRepository.findByUsername("paul_white");
                Users user8 = userRepository.findByUsername("susan_green");

                // Fetch all Categories by name
                // Using orElseThrow() is safer because it will stop the process if a category
                // is missing
                Todo_Category workCategory = categoryRepository.findByName("Work").orElseThrow();
                Todo_Category personalCategory = categoryRepository.findByName("Personal").orElseThrow();
                Todo_Category dailyCategory = categoryRepository.findByName("Daily").orElseThrow();
                Todo_Category healthCategory = categoryRepository.findByName("Health").orElseThrow();
                Todo_Category studyCategory = categoryRepository.findByName("Study").orElseThrow();
                Todo_Category urgentCategory = categoryRepository.findByName("Urgent").orElseThrow();
                Todo_Category homeCategory = categoryRepository.findByName("Home").orElseThrow();
                Todo_Category shoppingCategory = categoryRepository.findByName("Shopping").orElseThrow();

                // Fetch all Tags by name
                Todo_Tag urgentTag = tagRepository.findByName("Urgent").orElseThrow();
                Todo_Tag importantTag = tagRepository.findByName("Important").orElseThrow();
                Todo_Tag highPriorityTag = tagRepository.findByName("High Priority").orElseThrow();
                Todo_Tag lowPriorityTag = tagRepository.findByName("Low Priority").orElseThrow();
                Todo_Tag completedTag = tagRepository.findByName("Completed").orElseThrow();
                Todo_Tag pendingTag = tagRepository.findByName("Pending").orElseThrow();
                Todo_Tag followUpTag = tagRepository.findByName("Follow Up").orElseThrow();
                Todo_Tag reminderTag = tagRepository.findByName("Reminder").orElseThrow();

                // ====================================================================================
                // 2. CREATE ALL TODO OBJECTS INDIVIDUALLY
                // ====================================================================================

                // Note: Passing null for createdDate and lastUpdated lets @PrePersist handle
                // it.
                // The last argument is for attachments, which is null for most todos.

                Todos todo1 = new Todos(user1.getUsername(), "Work Task One", "Complete the work task",
                                Todos.Priority.HIGH, Todos.Status.PENDING, workCategory,
                                LocalDateTime.of(2024, 12, 24, 10, 0, 0), "Daily", "5",
                                null, null, null,
                                Arrays.asList(urgentTag, highPriorityTag, lowPriorityTag), null);

                // --- THIS IS THE TODO WITH THE ATTACHMENT ---
                // First, create the Todo object itself, passing an empty list for its
                // attachments.
                Todos todo2 = new Todos(user2.getUsername(), "Personal Task One", "Complete personal errands",
                                Todos.Priority.MEDIUM, Todos.Status.PENDING, personalCategory,
                                LocalDateTime.of(2024, 12, 24, 10, 0, 0), "Weekly", "10",
                                null, null, null,
                                Arrays.asList(importantTag, completedTag, pendingTag), new ArrayList<>());

                // Second, create the Attachment object that belongs to todo2.
                Todo_Attachment instructions = new Todo_Attachment("instructions.pdf", "instructions.pdf");

                // Third (CRITICAL STEP), set the parent (todo2) on the child (instructions).
                // This satisfies the "not-null" requirement for the 'todo' field.
                instructions.setTodo(todo2);

                // Fourth, add the fully prepared attachment to todo2's list of attachments.
                todo2.getAttachments().add(instructions);
                // --- ATTACHMENT LOGIC COMPLETE ---

                Todos todo3 = new Todos(user3.getUsername(), "Health Task", "Gym workout and health checkup",
                                Todos.Priority.LOW, Todos.Status.COMPLETED, healthCategory,
                                LocalDateTime.of(2024, 12, 24, 10, 0, 0), "Weekly", "15",
                                null, null, LocalDateTime.of(2024, 12, 24, 10, 20, 0),
                                Arrays.asList(completedTag, followUpTag), null);

                Todos todo4 = new Todos(user4.getUsername(), "Study Task", "Finish studying for exams",
                                Todos.Priority.HIGH, Todos.Status.PENDING, studyCategory,
                                LocalDateTime.of(2024, 12, 24, 10, 0, 0), "Monthly", "20",
                                null, null, null,
                                Arrays.asList(completedTag, pendingTag), null);

                Todos todo5 = new Todos(user5.getUsername(), "Urgent Task", "Complete the urgent task today",
                                Todos.Priority.HIGH, Todos.Status.PENDING, urgentCategory,
                                LocalDateTime.of(2024, 12, 24, 10, 0, 0), "Daily", "25",
                                null, null, null,
                                Arrays.asList(urgentTag, importantTag), null);

                Todos todo6 = new Todos(user6.getUsername(), "Home Task", "Organize the house",
                                Todos.Priority.MEDIUM, Todos.Status.IN_PROGRESS, homeCategory,
                                LocalDateTime.of(2024, 12, 24, 10, 0, 0), "Daily", "30",
                                null, null, null,
                                Arrays.asList(followUpTag, pendingTag), null);

                Todos todo7 = new Todos(user7.getUsername(), "Shopping Task", "Go for shopping",
                                Todos.Priority.LOW, Todos.Status.PENDING, shoppingCategory,
                                LocalDateTime.of(2024, 12, 24, 10, 0, 0), "Weekly", "35",
                                null, null, null,
                                Arrays.asList(highPriorityTag, lowPriorityTag), null);

                Todos todo8 = new Todos(user8.getUsername(), "Health Task Two", "Go for a morning run and exercise",
                                Todos.Priority.MEDIUM, Todos.Status.PENDING, dailyCategory,
                                LocalDateTime.of(2024, 12, 24, 10, 0, 0), "Monthly", "40",
                                null, null, null,
                                Arrays.asList(completedTag, pendingTag, reminderTag), null);

                // ====================================================================================
                // 3. SAVE ALL TODOS AT ONCE
                // ====================================================================================

                // Create a list containing all the Todos objects you just configured.
                List<Todos> todos = Arrays.asList(todo1, todo2, todo3, todo4, todo5, todo6, todo7, todo8);

                // This single saveAll command will save all 8 todos. Because of
                // CascadeType.ALL,
                // it will also automatically save the 'instructions' attachment for todo2.
                todoRepository.saveAll(todos);
                System.out.println("Todos seeded.");
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
