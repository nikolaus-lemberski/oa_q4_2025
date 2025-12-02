package com.example.todo.controller;

import com.example.todo.model.Todo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final Map<Long, Todo> todos = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @PostConstruct
    public void init() {
        todos.put(1L, new Todo(1L, "Learn Spring Boot", "Study Spring Boot 2.x fundamentals", false));
        todos.put(2L, new Todo(2L, "Build REST API", "Create a simple REST API for TODO management", false));
        todos.put(3L, new Todo(3L, "Complete modernization demo", "Prepare application for modernization presentation", true));
        idGenerator.set(3L);
    }

    @GetMapping
    public List<Todo> getAllTodos() {
        return new ArrayList<>(todos.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Todo todo = todos.get(id);
        if (todo != null) {
            return ResponseEntity.ok(todo);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        todo.setId(idGenerator.incrementAndGet());
        todos.put(todo.getId(), todo);
        return ResponseEntity.status(HttpStatus.CREATED).body(todo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
        Todo existingTodo = todos.get(id);
        if (existingTodo != null) {
            todo.setId(id);
            todos.put(id, todo);
            return ResponseEntity.ok(todo);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        Todo removed = todos.remove(id);
        if (removed != null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

