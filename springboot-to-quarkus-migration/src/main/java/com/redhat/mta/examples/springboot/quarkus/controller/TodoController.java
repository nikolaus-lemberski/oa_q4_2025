package com.redhat.mta.examples.springboot.quarkus.controller;

import com.redhat.mta.examples.springboot.quarkus.dto.TodoCreateRequest;
import com.redhat.mta.examples.springboot.quarkus.dto.TodoResponse;
import com.redhat.mta.examples.springboot.quarkus.dto.TodoUpdateRequest;
import com.redhat.mta.examples.springboot.quarkus.model.Todo;
import com.redhat.mta.examples.springboot.quarkus.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Todo Controller demonstrating Spring Web patterns for Quarkus migration.
 * 
 * Migration to Quarkus:
 * - Replace @RestController with @Path
 * - Replace @RequestMapping with @GET/@POST/@PUT/@DELETE + @Path
 * - Replace @PathVariable with @PathParam
 * - Replace @RequestParam with @QueryParam
 * - Replace @RequestBody with automatic (not needed in Quarkus)
 * - Replace ResponseEntity with JAX-RS Response
 * - Replace HttpStatus with Response.Status
 */
@RestController
@RequestMapping("/api/todos")
@Validated
public class TodoController {

    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    @Autowired
    private TodoService todoService;

    /**
     * Get all todos
     */
    @GetMapping
    public ResponseEntity<List<TodoResponse>> getAllTodos(
            @RequestParam(value = "completed", required = false) Boolean completed) {
        
        logger.debug("Getting all todos, completed={}", completed);
        
        List<Todo> todos;
        
        if (completed != null) {
            todos = todoService.findByCompleted(completed);
        } else {
            todos = todoService.findAll();
        }
        
        List<TodoResponse> todoResponses = todos.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(todoResponses);
    }

    /**
     * Get todo by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TodoResponse> getTodoById(@PathVariable @NotNull Long id) {
        logger.debug("Getting todo by ID: {}", id);
        
        Todo todo = todoService.findById(id);
        TodoResponse response = convertToResponse(todo);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Search todos by title
     */
    @GetMapping("/search")
    public ResponseEntity<List<TodoResponse>> searchTodos(@RequestParam("title") String title) {
        logger.debug("Searching todos by title: {}", title);
        
        List<Todo> todos = todoService.searchByTitle(title);
        List<TodoResponse> responses = todos.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(responses);
    }

    /**
     * Create new todo
     */
    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(@Valid @RequestBody TodoCreateRequest request) {
        logger.info("Creating new todo: {}", request.getTitle());
        
        Todo todo = convertFromCreateRequest(request);
        Todo createdTodo = todoService.createTodo(todo);
        TodoResponse response = convertToResponse(createdTodo);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update existing todo
     */
    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse> updateTodo(
            @PathVariable @NotNull Long id,
            @Valid @RequestBody TodoUpdateRequest request) {
        
        logger.info("Updating todo: {}", id);
        
        Todo todo = convertFromUpdateRequest(request);
        todo.setId(id);
        
        Todo updatedTodo = todoService.updateTodo(todo);
        TodoResponse response = convertToResponse(updatedTodo);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Toggle todo completion status
     */
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TodoResponse> toggleTodo(@PathVariable @NotNull Long id) {
        logger.info("Toggling completion status for todo: {}", id);
        
        Todo updatedTodo = todoService.toggleCompletion(id);
        TodoResponse response = convertToResponse(updatedTodo);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Delete todo
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable @NotNull Long id) {
        logger.info("Deleting todo: {}", id);
        todoService.deleteTodo(id);
        
        return ResponseEntity.noContent().build();
    }

    // Utility methods for conversion
    private TodoResponse convertToResponse(Todo todo) {
        TodoResponse response = new TodoResponse();
        response.setId(todo.getId());
        response.setTitle(todo.getTitle());
        response.setDescription(todo.getDescription());
        response.setCompleted(todo.getCompleted());
        response.setCreatedAt(todo.getCreatedAt());
        response.setUpdatedAt(todo.getUpdatedAt());
        return response;
    }

    private Todo convertFromCreateRequest(TodoCreateRequest request) {
        Todo todo = new Todo();
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setCompleted(false);
        return todo;
    }

    private Todo convertFromUpdateRequest(TodoUpdateRequest request) {
        Todo todo = new Todo();
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setCompleted(request.getCompleted());
        return todo;
    }
}

