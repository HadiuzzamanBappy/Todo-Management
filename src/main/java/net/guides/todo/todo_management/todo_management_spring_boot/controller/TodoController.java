package net.guides.todo.todo_management.todo_management_spring_boot.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import net.guides.todo.todo_management.todo_management_spring_boot.model.Notifications;
import net.guides.todo.todo_management.todo_management_spring_boot.model.Todo_Attachment;
import net.guides.todo.todo_management.todo_management_spring_boot.model.Todo_Category;
import net.guides.todo.todo_management.todo_management_spring_boot.model.Todo_Tag;
import net.guides.todo.todo_management.todo_management_spring_boot.model.Todos;
import net.guides.todo.todo_management.todo_management_spring_boot.model.Users;
import net.guides.todo.todo_management.todo_management_spring_boot.repository.AttachmentRepository;
import net.guides.todo.todo_management.todo_management_spring_boot.repository.CategoryRepository;
import net.guides.todo.todo_management.todo_management_spring_boot.repository.TagRepository;
import net.guides.todo.todo_management.todo_management_spring_boot.service.NotificationService;
import net.guides.todo.todo_management.todo_management_spring_boot.service.TodoService;
import net.guides.todo.todo_management.todo_management_spring_boot.service.UserService;

/**
 * The `TodoController` class in Java is responsible for handling requests
 * related to managing todo
 * items, including adding, updating, filtering, completing, and deleting todos,
 * as well as downloading
 * attachments and diagnosing payloads.
 */
@Controller
public class TodoController {

    @Autowired
    private TodoService todoService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    private Users getLoggedInUser(ModelMap modelMap) {
        Object princiObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (princiObject instanceof UserDetails) {
            String username = ((UserDetails) princiObject).getUsername();
            return userService.findUserByUsername(username);
        }

        return null;
    }

    @GetMapping("/list-todos")
    public String showTodos(
            @RequestParam(defaultValue = "0") int page,
            ModelMap modelMap) {
        String name = getLoggedInUser(modelMap).getUsername();
        String role = getLoggedInUser(modelMap).getRole();
        int pageSize = 6;

        Page<Todos> todosPage;
        if (role.equals("USER")) {
            todosPage = todoService.getTodosByUser(name, page, pageSize);
        } else {
            todosPage = todoService.getAllTodos(page, pageSize);
        }

        modelMap.put("todos", todosPage.getContent());
        modelMap.put("currentPage", todosPage.getNumber());
        modelMap.put("totalPages", todosPage.getTotalPages());
        modelMap.put("totalItems", todosPage.getTotalElements());

        List<Todo_Category> categories = categoryRepository.findAll();
        modelMap.addAttribute("categories", categories);

        modelMap.addAttribute("selectedPriority", null);
        modelMap.addAttribute("selectedStatus", null);
        modelMap.addAttribute("selectedCategory", null);

        modelMap.put("page_header", "Todo | All");
        return "list-todos";
    }

    @GetMapping("/filter-todos")
    public String showListTodos(
            @RequestParam(required = false) Todos.Priority priority,
            @RequestParam(required = false) Todos.Status status,
            @RequestParam(required = false) String category,
            ModelMap modelMap) {

        List<Todos> todos = todoService.filterTodos(priority, status, category);
        modelMap.addAttribute("todos", todos);

        List<Todo_Category> categories = categoryRepository.findAll();
        modelMap.addAttribute("categories", categories);

        modelMap.addAttribute("selectedPriority", priority != null ? priority.name() : null);
        modelMap.addAttribute("selectedStatus", status != null ? status.name() : null);
        modelMap.addAttribute("selectedCategory", category != "" ? category : null);

        modelMap.put("page_header", "Todo | Filtered");
        return "list-todos";
    }

    @GetMapping("/add-todo")
    public String showAddTodoPage(ModelMap modelMap) {
        Todos todo = new Todos();
        todo.setRecurrence("Once");
        todo.setDueTime(LocalDateTime.now());

        List<Todo_Category> categories = categoryRepository.findAll();

        modelMap.put("page_header", "Todo | Add");
        modelMap.put("type", "Add");
        modelMap.put("formAction", "/save-todo");
        modelMap.put("todo", todo);
        modelMap.put("categories", categories);

        return "add-todo";
    }

    @GetMapping("/update-todo")
    public String showUpdateTodoPage(@RequestParam long id, ModelMap modelMap) {
        Todos todo = todoService.getTodoById(id);
        List<Todo_Category> categories = categoryRepository.findAll();

        modelMap.put("page_header", "Todo | Update");
        modelMap.put("type", "Update");
        modelMap.put("formAction", "/save-todo");
        modelMap.put("todo", todo);
        modelMap.put("categories", categories);

        return "add-todo";
    }

    @GetMapping("/delete-todo")
    public String deleteTodo(@RequestParam long id, ModelMap modelMap) {
        Todos todo = todoService.getTodoById(id);
        String name = getLoggedInUser(modelMap).getUsername();
        if (todo != null) {
            for (Todo_Attachment attachment : todo.getAttachments()) {
                Path filePath = Paths.get("uploads", name, attachment.getFilePath());
                try {
                    Files.deleteIfExists(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            todo.getAttachments().clear();
            todoService.deleteTodo(id);
        }
        return "redirect:/list-todos";
    }

    @GetMapping("/complete-todo")
    public String completeTodo(@RequestParam long id, ModelMap modelMap) {
        Todos todo = todoService.getTodoById(id);
        todo.setCompletionDate(LocalDateTime.now());
        todo.setStatus(Todos.Status.COMPLETED);
        todoService.updateTodo(todo);

        return "redirect:/list-todos";
    }

    @PostMapping("/save-todo")
    public String saveOrUpdateTodo(
            @ModelAttribute("todo") Todos todo,
            @RequestParam(value = "newCategory", required = false) String newCategory,
            @RequestParam(value = "allTags", required = false) List<String> tags,
            @RequestParam(value = "existingAttachmentIds", required = false) List<Long> existingAttachmentIds,
            @RequestParam(value = "deletedAttachmentIds", required = false) List<Long> deletedAttachmentIds,
            @RequestParam(value = "newAttachments", required = false) MultipartFile[] newFiles,
            BindingResult result,
            ModelMap modelMap) throws Exception {

        if (result.hasErrors()) {
            List<Todo_Category> categories = categoryRepository.findAll();

            modelMap.put("page_header", todo.getId() != null ? "Todo | Update" : "Todo | Add");
            modelMap.put("type", todo.getId() != null ? "Update" : "Add");
            modelMap.put("formAction", todo.getId() != null ? "/update-todo" : "/save-todo");
            modelMap.put("todo", todo.getId() == null ? new Todos() : todo);
            modelMap.put("categories", categories);

            return "add-todo";
        }

        String username = getLoggedInUser(modelMap).getUsername();
        todo.setUserName(username);

        if (newCategory != null && !newCategory.isEmpty()) {
            Todo_Category category = new Todo_Category();
            category.setName(newCategory);
            categoryRepository.save(category);
            todo.setCategory(category);
        } else if (todo.getCategory() != null && todo.getCategory().getId() != null) {
            todo.setCategory(categoryRepository.findById(todo.getCategory().getId()).orElse(null));
        }

        if (tags != null && !tags.isEmpty()) {
            List<Todo_Tag> todoTags = new ArrayList<>();
            for (String tagName : tags) {
                tagName = tagName.trim();
                if (!tagName.isEmpty()) {
                    Todo_Tag existingTag = tagRepository.findByName(tagName).orElse(null);
                    if (existingTag == null) {
                        Todo_Tag newTag = new Todo_Tag();
                        newTag.setName(tagName);
                        newTag.setCreatedDate(LocalDateTime.now());
                        tagRepository.save(newTag);
                        todoTags.add(newTag);
                    } else {
                        todoTags.add(existingTag);
                    }
                }
            }
            todo.setTags(todoTags);
        }

        List<Todo_Attachment> updatedAttachments = new ArrayList<>();

        if (existingAttachmentIds != null && !existingAttachmentIds.isEmpty()) {
            for (Long attachmentId : existingAttachmentIds) {
                if (deletedAttachmentIds == null || !deletedAttachmentIds.contains(attachmentId)) {
                    Todo_Attachment attachment = attachmentRepository.findById(attachmentId).orElse(null);
                    if (attachment != null) {
                        updatedAttachments.add(attachment);
                    }
                }
            }
        }

        if (newFiles != null && newFiles.length > 0) {
            for (MultipartFile file : newFiles) {
                if (!file.isEmpty()) {
                    try {
                        Path userDirectory = Paths.get("uploads", username);
                        if (!Files.exists(userDirectory)) {
                            Files.createDirectories(userDirectory);
                        }
                        Path destinationFile = userDirectory.resolve(file.getOriginalFilename()).normalize()
                                .toAbsolutePath();

                        if (!destinationFile.getParent().equals(userDirectory.toAbsolutePath())) {
                            throw new Exception("Cannot store file outside current directory.");
                        }

                        try (InputStream inputStream = file.getInputStream()) {
                            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                        }

                        Todo_Attachment newAttachment = Todo_Attachment.builder()
                                .fileName(file.getOriginalFilename())
                                .filePath(file.getOriginalFilename())
                                .todo(todo)
                                .build();
                        updatedAttachments.add(newAttachment);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (deletedAttachmentIds != null && !deletedAttachmentIds.isEmpty()) {
            for (Long deletedId : deletedAttachmentIds) {
                Todo_Attachment deletedAttachment = attachmentRepository.findById(deletedId).orElse(null);
                if (deletedAttachment != null) {
                    try {
                        Path userDirectory = Paths.get("uploads", username);
                        Path fileToDelete = userDirectory.resolve(deletedAttachment.getFilePath()).normalize();
                        Files.deleteIfExists(fileToDelete);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    attachmentRepository.delete(deletedAttachment);
                }
            }
        }

        todo.setAttachments(updatedAttachments);

        if (todo.getId() == null) {
            todo.setCreatedDate(LocalDateTime.now());
            todo.setLastUpdated(LocalDateTime.now());
            todoService.saveTodo(todo);

            notificationService.createNotification(
                    "A new todo was created by " + username + ": " + todo.getTitle(),
                    Notifications.NotificationType.TASK_UPDATE,
                    userService.findUserByUsername(username));

            notificationService.createNotification(
                    "A new todo was created by " + username + ": " + todo.getTitle(),
                    Notifications.NotificationType.SYSTEM,
                    null);
        } else {
            Todos existingTodo = todoService.getTodoById(todo.getId());

            existingTodo.setTitle(todo.getTitle());
            existingTodo.setDescription(todo.getDescription());
            existingTodo.setPriority(todo.getPriority());
            existingTodo.setStatus(todo.getStatus());
            existingTodo.setCategory(todo.getCategory());
            existingTodo.setDueTime(todo.getDueTime());
            existingTodo.setRecurrence(todo.getRecurrence());
            existingTodo.setReminder(todo.getReminder());
            existingTodo.setCompletionDate(todo.getCompletionDate());

            existingTodo.getTags().clear();
            if (todo.getTags() != null) {
                existingTodo.getTags().addAll(todo.getTags());
            }

            existingTodo.getAttachments().clear();
            if (todo.getAttachments() != null) {
                for (Todo_Attachment attachment : todo.getAttachments()) {
                    attachment.setTodo(existingTodo);
                    existingTodo.getAttachments().add(attachment);
                }
            }
            existingTodo.setLastUpdated(LocalDateTime.now());

            todoService.updateTodo(existingTodo);
        }

        return "redirect:/list-todos";
    }

    @PostMapping("/diagnose")
    public String diagnosePayload(HttpServletRequest request) {
        request.getParameterMap().forEach((key, value) -> {
            System.out.println(key + ": " + Arrays.toString(value));
        });
        return "redirect:/";
    }

    @GetMapping("/download/{filePath}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filePath, ModelMap modelMap)
            throws FileNotFoundException {
        String name = getLoggedInUser(modelMap).getUsername();
        Path file = Paths.get("uploads", name, filePath).toAbsolutePath().normalize();

        Resource resource = null;
        try {
            resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new FileNotFoundException("File not found " + filePath);
            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("File not found " + filePath);
        }
    }

}
