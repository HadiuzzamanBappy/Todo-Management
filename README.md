
# Todo Management System
A robust and user-friendly Todo Management System built using Spring Boot. This project offers role-based access control, notifications, and a suite of features for efficient task and user management.

![Admin Preview](/uploads/demo/admin.png)
![User Preview](/uploads/demo/user.png)

## Features

### User Management:
Admin: Manage all users, view their activities.
User: Manage personal profile and todos.
### TODO Management:
Add, edit, delete, and view todos.Filter todos by priority, status, category, due date, etc.Notifications for overdue todos and other events.Pagination for Todos and Users.
### Notifications:
Separate notifications for Admin and User roles.Read and unread notifications.
### Security:
Role-based access control with CSRF protection.Password-based authentication.
### Dashboard:
Summary of key metrics (e.g., total users, todos, notifications).Latest notifications and users.

---

## Technology Stack

| Component     | Technology           | Purpose                            |
|---------------|----------------------|------------------------------------|
| Backend       | Spring Boot          | Core application framework.        |
| Security      | Spring Security      | Role-based access control.         |
| ORM           | Hibernate/JPA        | Database interaction.              |
| Frontend      | Thymeleaf, Bootstrap | Dynamic UI rendering and styling.  |
| Database      | MySQL                | Persistent data storage.           |

---

## Setup Instructions

### Prerequisites
1. Install **JDK 17** or higher.
2. Install **MySQL**.
3. Install **Maven**.
4. Install **XAMPP**(optional).
5. Install **Spring Boot v3.1+**.

### Database Configuration
1. Log in to your MySQL instance and create a database named `todo`: CREATE DATABASE todo;
2. Configure the database connection in `src/main/resources/application.properties`:
   spring.datasource.url=jdbc:mysql://localhost:3306/todo
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update

### Build and Run the Application
1. Clone the repository: 
   link: https://github.com/HadiuzzamanBappy/Todo_Management
   command: git clone <repository_url>
            cd <repository_folder>
2. Build the project with Maven: mvn clean install
3. Start the application: mvn spring-boot:run
4. Open your browser and navigate to [http://localhost:8080].

---
## Project Structure

- **Controllers:** Manage HTTP requests and responses.
- **Services:** Contain the business logic.
- **Repositories:** Facilitate database operations.
- **Models:** Represent database entities.
- **Views:** Thymeleaf templates for rendering pages.

---
## Usage

### Admin Credentials:
- Username: admin
- Password: 123456
### User Registration:
- Users can register themselves and manage their todos.

----
## APIs

### Notification APIs
- GET /notifications/view - View notifications, optionally filtered by status.
- POST /notifications/broadcast - Broadcast a notification to all users or a specific user.
- GET /notifications/mark-as-read - Mark a specific notification as read.
- GET /notifications/delete - Delete a specific notification.

### TODO APIs
- GET /list-todos - List all todos for the logged-in user or admin.
- GET /filter-todos - Filter todos based on priority, status, or category.
- GET /add-todo - Show the page to add a new todo.
- GET /update-todo - Show the page to update an existing todo.
- GET /delete-todo - Delete a specific todo and its attachments.
- GET /complete-todo - Mark a todo as completed.
- POST /save-todo - Save a new or updated todo.
- GET /download/{filePath} - Download an attachment associated with a todo.

-----
## Future Enhancements

- Add email/SMS notification support.
- Introduce dark mode for UI.

---
## Contributing

I welcome contributions!
- Fork the repository and create a branch for your feature or bugfix.
- Submit a pull request with a clear description of your changes.

---
### Contact

If you encounter issues or have suggestions, please reach out via **hbappy79@gmail.com**.
