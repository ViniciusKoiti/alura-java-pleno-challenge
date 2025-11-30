package br.com.alura.AluraFake.task.service;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.task.OpenTextTask;
import br.com.alura.AluraFake.task.Task;
import br.com.alura.AluraFake.task.TaskRepository;
import br.com.alura.AluraFake.user.Role;
import br.com.alura.AluraFake.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskOrderServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskOrderService taskOrderService;

    private Course course;
    private Task existingTask;

    @BeforeEach
    void setUp() {
        User instructor = new User("João Silva", "joao@alura.com.br", Role.INSTRUCTOR);
        course = new Course("Java Fundamentals", "Learn Java basics", instructor);
        existingTask = new OpenTextTask("What is Java?", 2, course);
    }

    @Test
    void handleOrderConflict__should_increment_positions_when_conflict_exists() {
        // Given
        Long courseId = 1L;
        Integer requestedOrder = 2;
        
        when(taskRepository.findByCourseIdAndOrderPosition(courseId, requestedOrder))
                .thenReturn(Optional.of(existingTask));

        // When
        taskOrderService.handleOrderConflict(courseId, requestedOrder);

        // Then
        verify(taskRepository).findByCourseIdAndOrderPosition(courseId, requestedOrder);
        verify(taskRepository).incrementOrderPositionsFromOrder(courseId, requestedOrder);
    }

    @Test
    void handleOrderConflict__should_not_increment_when_no_conflict() {
        // Given
        Long courseId = 1L;
        Integer requestedOrder = 3;
        
        when(taskRepository.findByCourseIdAndOrderPosition(courseId, requestedOrder))
                .thenReturn(Optional.empty());

        // When
        taskOrderService.handleOrderConflict(courseId, requestedOrder);

        // Then
        verify(taskRepository).findByCourseIdAndOrderPosition(courseId, requestedOrder);
        verify(taskRepository, never()).incrementOrderPositionsFromOrder(any(), any());
    }

    @Test
    void isSequenceContinuous__should_return_repository_result_when_true() {
        // Given
        Long courseId = 1L;
        when(taskRepository.isOrderSequenceContinuous(courseId)).thenReturn(true);

        // When
        boolean result = taskOrderService.isSequenceContinuous(courseId);

        // Then
        assertThat(result).isTrue();
        verify(taskRepository).isOrderSequenceContinuous(courseId);
    }

    @Test
    void isSequenceContinuous__should_return_repository_result_when_false() {
        // Given
        Long courseId = 1L;
        when(taskRepository.isOrderSequenceContinuous(courseId)).thenReturn(false);

        // When
        boolean result = taskOrderService.isSequenceContinuous(courseId);

        // Then
        assertThat(result).isFalse();
        verify(taskRepository).isOrderSequenceContinuous(courseId);
    }

    @Test
    void hasStatementConflict__should_return_true_when_statement_exists() {
        // Given
        Long courseId = 1L;
        String statement = "What is Java?";
        when(taskRepository.existsByCourseIdAndStatement(courseId, statement)).thenReturn(true);

        // When
        boolean result = taskOrderService.hasStatementConflict(courseId, statement);

        // Then
        assertThat(result).isTrue();
        verify(taskRepository).existsByCourseIdAndStatement(courseId, statement);
    }

    @Test
    void hasStatementConflict__should_return_false_when_statement_does_not_exist() {
        // Given
        Long courseId = 1L;
        String statement = "What is Spring Boot?";
        when(taskRepository.existsByCourseIdAndStatement(courseId, statement)).thenReturn(false);

        // When
        boolean result = taskOrderService.hasStatementConflict(courseId, statement);

        // Then
        assertThat(result).isFalse();
        verify(taskRepository).existsByCourseIdAndStatement(courseId, statement);
    }

    @Test
    void handleOrderConflict__should_handle_null_course_id() {
        // Given
        Long courseId = null;
        Integer requestedOrder = 1;
        
        when(taskRepository.findByCourseIdAndOrderPosition(courseId, requestedOrder))
                .thenReturn(Optional.empty());

        // When
        taskOrderService.handleOrderConflict(courseId, requestedOrder);

        // Then
        verify(taskRepository).findByCourseIdAndOrderPosition(courseId, requestedOrder);
        verify(taskRepository, never()).incrementOrderPositionsFromOrder(any(), any());
    }

    @Test
    void handleOrderConflict__should_handle_null_requested_order() {
        // Given
        Long courseId = 1L;
        Integer requestedOrder = null;
        
        when(taskRepository.findByCourseIdAndOrderPosition(courseId, requestedOrder))
                .thenReturn(Optional.empty());

        // When
        taskOrderService.handleOrderConflict(courseId, requestedOrder);

        // Then
        verify(taskRepository).findByCourseIdAndOrderPosition(courseId, requestedOrder);
        verify(taskRepository, never()).incrementOrderPositionsFromOrder(any(), any());
    }
}