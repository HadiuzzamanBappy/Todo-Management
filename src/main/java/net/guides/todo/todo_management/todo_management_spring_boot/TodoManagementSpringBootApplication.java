package net.guides.todo.todo_management.todo_management_spring_boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * This class represents the main entry point for a Spring Boot application for managing todos.
 */
@EnableCaching
@SpringBootApplication
public class TodoManagementSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoManagementSpringBootApplication.class, args);
	}

}
