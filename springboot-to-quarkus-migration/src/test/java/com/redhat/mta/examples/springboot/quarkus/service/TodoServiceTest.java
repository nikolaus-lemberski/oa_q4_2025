package com.redhat.mta.examples.springboot.quarkus.service;

import com.redhat.mta.examples.springboot.quarkus.exception.ResourceNotFoundException;
import com.redhat.mta.examples.springboot.quarkus.model.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Todo Service Test demonstrating Spring Boot testing patterns for Quarkus migration.
 * 
 * Migration to Quarkus:
 * - Replace @SpringBootTest with @QuarkusTest
 * - Replace @Autowired with @Inject
 */
@SpringBootTest
class TodoServiceTest {

    @Autowired
    private TodoService todoService;

    private Todo testTodo;

    @BeforeEach
    void setUp() {
        testTodo = new Todo();
        testTodo.setId(1L);
        testTodo.setTitle("Test Todo");
        testTodo.setDescription("Test Description");
        testTodo.setCompleted(false);
    }

    @Test
    void findById_WithValidId_ShouldReturnTodo() {
        // Given - service is initialized with sample data
        Long existingId = 1L;

        // When
        Todo result = todoService.findById(existingId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(existingId);
        assertThat(result.getTitle()).isNotNull();
    }

    @Test
    void findById_WithInvalidId_ShouldThrowException() {
        // When & Then
        assertThatThrownBy(() -> todoService.findById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Todo not found with ID: 999");
    }

    @Test
    void createTodo_WithValidData_ShouldCreateTodo() {
        // Given
        Todo newTodo = new Todo("New Test Todo", "New Test Description");

        // When
        Todo result = todoService.createTodo(newTodo);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getTitle()).isEqualTo("New Test Todo");
        assertThat(result.getCreatedAt()).isNotNull();
    }

    @Test
    void updateTodo_WithValidData_ShouldUpdateTodo() {
        // Given - get an existing todo
        Todo existing = todoService.findById(1L);
        Todo updatedTodo = new Todo();
        updatedTodo.setId(existing.getId());
        updatedTodo.setTitle("Updated Todo");
        updatedTodo.setDescription("Updated Description");
        updatedTodo.setCompleted(true);

        // When
        Todo result = todoService.updateTodo(updatedTodo);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Updated Todo");
        assertThat(result.getCompleted()).isTrue();
    }

    @Test
    void toggleCompletion_WithValidId_ShouldToggleCompletion() {
        // Given - get an existing todo
        Todo existing = todoService.findById(1L);
        boolean originalStatus = existing.getCompleted();

        // When
        Todo result = todoService.toggleCompletion(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCompleted()).isEqualTo(!originalStatus);
    }

    @Test
    void deleteTodo_WithValidId_ShouldDeleteTodo() {
        // Given - create a new todo to delete
        Todo newTodo = new Todo("To Delete", "Will be deleted");
        Todo created = todoService.createTodo(newTodo);
        Long idToDelete = created.getId();

        // When
        todoService.deleteTodo(idToDelete);

        // Then - verify it's deleted
        assertThatThrownBy(() -> todoService.findById(idToDelete))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

