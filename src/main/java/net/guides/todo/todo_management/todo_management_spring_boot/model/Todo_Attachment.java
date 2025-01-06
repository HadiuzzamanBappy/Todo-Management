package net.guides.todo.todo_management.todo_management_spring_boot.model;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

/**
 * The `Todo_Attachment` class represents an entity for attachments related to todos, with properties
 * for file path, file name, and a many-to-one relationship with the `Todos` entity.
 */
@Entity
@Table(name = "attachments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo_Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_path", nullable = false)
    @NotEmpty(message = "File path cannot be empty")
    private String filePath;

    @Column(name = "file_name", nullable = false)
    @NotEmpty(message = "File name cannot be empty")
    private String fileName;

    // To map to todos table (Many-to-One)
    @ManyToOne
    @JoinColumn(name = "todo_id", nullable = false)
    @JsonIgnore
    private Todos todo;

    @Override
    public String toString() {
        return "Attachment{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }

    public Todo_Attachment(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
    }
}
