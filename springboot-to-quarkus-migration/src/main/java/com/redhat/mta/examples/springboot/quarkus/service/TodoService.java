package com.redhat.mta.examples.springboot.quarkus.service;

import com.redhat.mta.examples.springboot.quarkus.exception.ResourceNotFoundException;
import com.redhat.mta.examples.springboot.quarkus.model.Todo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Todo Service demonstrating Spring patterns for Quarkus migration.
 * 
 * Migration to Quarkus:
 * - Replace @Service with @ApplicationScoped
 * - Replace @Autowired with @Inject
 * - Replace @PostConstruct with @PostConstruct (same in Quarkus)
 */
@Service
@Validated
public class TodoService {

    private static final Logger logger = LoggerFactory.getLogger(TodoService.class);

    private final Set<Todo> todos = new HashSet<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostConstruct
    public void init() {
        // Initialize with some sample data
        createTodo(new Todo("Learn Spring Boot", "Study Spring Boot fundamentals and best practices"));
        createTodo(new Todo("Migrate to Quarkus", "Plan and execute migration from Spring Boot to Quarkus"));
        createTodo(new Todo("Review Quarkus Documentation", "Read through Quarkus guides and migration patterns"));
        Todo completed = new Todo("Setup Development Environment", "Configure IDE and tools for Quarkus development");
        completed.setCompleted(true);
        createTodo(completed);
        logger.info("Initialized TodoService with {} sample todos", todos.size());
    }

    /**
     * Find all todos
     */
    public List<Todo> findAll() {
        logger.debug("Finding all todos");
        return new ArrayList<>(todos);
    }

    /**
     * Find todo by ID
     */
    public Todo findById(@NotNull Long id) {
        logger.debug("Finding todo by ID: {}", id);
        return todos.stream()
                .filter(todo -> Objects.equals(todo.getId(), id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with ID: " + id));
    }

    /**
     * Find todos by completion status
     */
    public List<Todo> findByCompleted(Boolean completed) {
        logger.debug("Finding todos by completion status: {}", completed);
        return todos.stream()
                .filter(todo -> Objects.equals(todo.getCompleted(), completed))
                .collect(Collectors.toList());
    }


    /**
     * Search todos by title
     */
    public List<Todo> searchByTitle(String title) {
        logger.debug("Searching todos by title: {}", title);
        String searchTerm = title.toLowerCase();
        return todos.stream()
                .filter(todo -> todo.getTitle() != null && todo.getTitle().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }

    /**
     * Create new todo
     */
    public Todo createTodo(@Valid Todo todo) {
        logger.info("Creating new todo: {}", todo.getTitle());
        
        // Generate ID and set timestamps
        todo.setId(idGenerator.getAndIncrement());
        LocalDateTime now = LocalDateTime.now();
        todo.setCreatedAt(now);
        todo.setUpdatedAt(now);
        
        todos.add(todo);
        logger.info("Todo created successfully with ID: {}", todo.getId());
        return todo;
    }

    /**
     * Update existing todo
     */
    public Todo updateTodo(@Valid Todo todo) {
        logger.info("Updating todo: {}", todo.getId());
        
        Todo existingTodo = findById(todo.getId());
        
        // Update fields
        existingTodo.setTitle(todo.getTitle());
        existingTodo.setDescription(todo.getDescription());
        existingTodo.setCompleted(todo.getCompleted());
        existingTodo.setUpdatedAt(LocalDateTime.now());

        logger.info("Todo updated successfully: {}", existingTodo.getId());
        return existingTodo;
    }

    /**
     * Toggle todo completion status
     */
    public Todo toggleCompletion(@NotNull Long id) {
        logger.info("Toggling completion status for todo: {}", id);
        
        Todo todo = findById(id);
        todo.setCompleted(!todo.getCompleted());
        todo.setUpdatedAt(LocalDateTime.now());
        
        logger.info("Todo completion status toggled successfully: {}", id);
        return todo;
    }

    /**
     * Delete todo by ID
     */
    public void deleteTodo(@NotNull Long id) {
        logger.info("Deleting todo: {}", id);
        
        Todo todo = findById(id);
        todos.remove(todo);
        
        logger.info("Todo deleted successfully: {}", id);
    }
}
