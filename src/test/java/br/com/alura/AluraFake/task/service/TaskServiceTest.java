package br.com.alura.AluraFake.task.service;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.course.CourseRepository;
import br.com.alura.AluraFake.course.Status;
import br.com.alura.AluraFake.task.OpenTextTask;
import br.com.alura.AluraFake.task.TaskRepository;
import br.com.alura.AluraFake.task.dto.OpenTextTaskDTO;
import br.com.alura.AluraFake.task.service.impl.TaskServiceImpl;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Course course;
    private OpenTextTaskDTO validTaskDTO;

    @BeforeEach
    void setUp() {
        User instructor = new User("Paulo Silva", "paulo@alura.com.br", Role.INSTRUCTOR);
        course = new Course("Java Fundamentals", "Learn Java basics", instructor);
        
        validTaskDTO = new OpenTextTaskDTO(1L, "What you think about Java?", 1);
    }

    @Test
    void createOpenTextTask__should_create_task_successfully() {
        // Given
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        
        OpenTextTask savedTask = new OpenTextTask("What you think about Java?", 1, course);
        when(taskRepository.save(any(OpenTextTask.class))).thenReturn(savedTask);

        // When
        OpenTextTaskDTO result = taskService.createOpenTextTask(validTaskDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCourseId()).isEqualTo(1L);
        assertThat(result.getStatement()).isEqualTo("What you think about Java?");
        assertThat(result.getOrder()).isEqualTo(1);
        
        verify(courseRepository).findById(1L);
        verify(taskRepository).save(any(OpenTextTask.class));
    }

    @Test
    void createOpenTextTask__should_throw_exception_when_course_not_found() {
        // Given
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> taskService.createOpenTextTask(validTaskDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Course not found with id: 1");
        
        verify(courseRepository).findById(1L);
        verify(taskRepository, never()).save(any());
    }

}