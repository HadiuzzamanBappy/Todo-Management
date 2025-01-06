package net.guides.todo.todo_management.todo_management_spring_boot.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.guides.todo.todo_management.todo_management_spring_boot.model.Users;
import net.guides.todo.todo_management.todo_management_spring_boot.service.UserService;

/**
 * The UserController class in Java handles user-related operations such as listing, adding, updating,
 * and deleting users, as well as managing user profiles.
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/list-users")
    public String filterUsers(@RequestParam(required = false) Boolean active,@RequestParam(defaultValue = "0") int page, ModelMap modelMap) {
        Page<Users> users;
        int pageSize = 6;
        Pageable pageable = PageRequest.of(page, pageSize);
    
        if (active == null) {
            users = userService.findPageableAllUsers(pageable);
        } else {
            users = userService.filterUsersByStatus(pageable,active);
        }

        modelMap.put("users", users.getContent());
        modelMap.put("currentPage", users.getNumber());
        modelMap.put("totalPages", users.getTotalPages());
        modelMap.put("totalItems", users.getTotalElements());
        modelMap.addAttribute("filterActive", active);
    
        modelMap.put("page_header", "Todo | Users");
        return "list-users";
    }

    @GetMapping("/add-user")
    public String showAddUserPage(ModelMap modelMap) {
        modelMap.put("page_header", "Todo | Add User");
        modelMap.put("type", "Add");
        modelMap.put("formAction", "/add-user");
        modelMap.put("user", new Users());
        return "profile";
    }

    @PostMapping("/add-user")
    public String customAddUser(ModelMap modelMap, Users users) {
        users.setPassword("123456");
        boolean success = userService.saveUser(users);
        if (!success) {
            modelMap.put("error", "Username or email already exists");
            return "auth";
        }
        return "redirect:/list-users";
    }

    @GetMapping("/update-user")
    public String showUpdateUserPage(@RequestParam long id, ModelMap modelMap) {
        Users user = userService.findUserById(id);
        modelMap.put("page_header", "Todo | Edit User");
        modelMap.put("type", "Update");
        modelMap.put("formAction", "/update-user");
        modelMap.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/update-user")
    public String updateUser(@Valid @ModelAttribute("user") Users user,BindingResult bindingResult,ModelMap modelMap){
        if (bindingResult.hasErrors()) {
            modelMap.put("page_header", "Todo | Edit User");
            modelMap.put("type", "Update");
            modelMap.put("formAction", "/update-user");
            return "list-users";
        }

        Users exstUser = userService.findUserById(user.getId());

        exstUser.setName(user.getName());
        exstUser.setEmail(user.getEmail());
        exstUser.setRole(user.getRole());
        exstUser.setEnabled(user.isEnabled());

        userService.updateUser(exstUser);
        return "redirect:/list-users";
    }

    @GetMapping("/delete-user")
    public String deleteTodo(@RequestParam long id) {
        userService.deleteUser(id);
        return "redirect:/list-users";
    }

    @GetMapping("/profile")
    public String showProfile(@RequestParam long id, ModelMap modelMap) {
        Users user = userService.findUserById(id);
        modelMap.put("page_header", "Todo | Profile");
        modelMap.put("type", "Manage");
        modelMap.put("formAction", "/profile-update");
        modelMap.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile-update")
    public String updateUserProfile(@Valid @ModelAttribute("user") Users user,BindingResult bindingResult,ModelMap modelMap){
        if (bindingResult.hasErrors()) {
            modelMap.put("page_header", "Todo | Profile");
            return "profile";
        }

        Users exstUser = userService.findUserById(user.getId());

        exstUser.setName(user.getName());
        exstUser.setEmail(user.getEmail());

        if(user.getPhone() != null){
            exstUser.setPhone(user.getPhone());
        }
        if(user.getPassword() != null){
            exstUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userService.updateUser(exstUser);
        return "redirect:/";
    }
}
