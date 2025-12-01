package com.redhat.mta.examples.springboot.quarkus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.mta.examples.springboot.quarkus.dto.TodoCreateRequest;
import com.redhat.mta.examples.springboot.quarkus.model.Todo;
import com.redhat.mta.examples.springboot.quarkus.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Todo Controller Test demonstrating Spring Boot testing patterns for Quarkus migration.
 * 
 * Migration to Quarkus:
 * - Replace @SpringBootTest with @QuarkusTest
 * - Replace @MockBean with @InjectMock
 * - Replace MockMvc with RestAssured
 */
@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired(required = false)
    private ObjectMapper objectMapper;
    
    private ObjectMapper testObjectMapper;

    @MockBean
    private TodoService todoService;

    private Todo testTodo;
    private TodoCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        testObjectMapper = objectMapper != null ? objectMapper : new ObjectMapper();
        
        testTodo = new Todo();
        testTodo.setId(1L);
        testTodo.setTitle("Test Todo");
        testTodo.setDescription("Test Description");
        testTodo.setCompleted(false);
        testTodo.setCreatedAt(LocalDateTime.now());
        testTodo.setUpdatedAt(LocalDateTime.now());

        createRequest = new TodoCreateRequest();
        createRequest.setTitle("New Todo");
        createRequest.setDescription("New Description");
    }

    @Test
    void getAllTodos_ShouldReturnTodos() throws Exception {
        // Given
        List<Todo> todos = Arrays.asList(testTodo);
        when(todoService.findAll()).thenReturn(todos);

        // When & Then
        mockMvc.perform(get("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Test Todo"))
                .andExpect(jsonPath("$[0].description").value("Test Description"));
    }

    @Test
    void getTodoById_WithValidId_ShouldReturnTodo() throws Exception {
        // Given
        when(todoService.findById(1L)).thenReturn(testTodo);

        // When & Then
        mockMvc.perform(get("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Todo"))
                .andExpect(jsonPath("$.description").value("Test Description"));
    }

    @Test
    void createTodo_WithValidData_ShouldCreateTodo() throws Exception {
        // Given
        when(todoService.createTodo(any(Todo.class))).thenReturn(testTodo);

        // When & Then
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testObjectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Todo"))
                .andExpect(jsonPath("$.description").value("Test Description"));
    }

    @Test
    void createTodo_WithInvalidData_ShouldReturn400() throws Exception {
        // Given
        createRequest.setTitle(""); // Invalid title

        // When & Then
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testObjectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").exists());
    }

    @Test
    void toggleTodo_WithValidId_ShouldToggleCompletion() throws Exception {
        // Given
        testTodo.setCompleted(true);
        when(todoService.toggleCompletion(1L)).thenReturn(testTodo);

        // When & Then
        mockMvc.perform(patch("/api/todos/1/toggle")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void deleteTodo_WithValidId_ShouldDeleteTodo() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}

