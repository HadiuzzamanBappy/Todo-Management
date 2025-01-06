package net.guides.todo.todo_management.todo_management_spring_boot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.guides.todo.todo_management.todo_management_spring_boot.model.Todos;
import net.guides.todo.todo_management.todo_management_spring_boot.repository.TodoRepository;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Cacheable(value = "todos", key = "#usernametodo")
    public Page<Todos> getTodosByUser(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return todoRepository.findByUserName(username, pageable);
    }

    @Cacheable(value = "todos", key = "#alltodo")
    public Page<Todos> getAllTodos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return todoRepository.findAll(pageable);
    }

    @Cacheable(value = "todos", key = "#username")
    public Todos getTodoById(long id) {
        return todoRepository.findById(id);
    }

    public void updateTodo(Todos todo) {
        todoRepository.save(todo);
    }

    public void saveTodo(Todos todo) {
        todoRepository.save(todo);
    }

    public void deleteTodo(long id) {
        todoRepository.deleteById(id);
    }  

    public long getTotalTodoCount() {
        return todoRepository.count();
    }

    public long getOverdueTodoCount() {
        return todoRepository.countOverdueTodos();
    }

    public long getCompletedTodoCount() {
        return todoRepository.countByStatus(Todos.Status.COMPLETED);
    }

    public long getPendingTodoCount() {
        return todoRepository.countByStatus(Todos.Status.PENDING);
    }

    public long getOngoingTodoCount() {
        return todoRepository.countByStatus(Todos.Status.IN_PROGRESS);
    }

    @Cacheable(value = "todos", key = "#filtertodo")
    public List<Todos> filterTodos(Todos.Priority priority, Todos.Status status, String category) {
        return todoRepository.filterTodos(priority, status, category);
    }    
}
