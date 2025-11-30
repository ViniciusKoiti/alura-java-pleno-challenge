package br.com.alura.AluraFake.task.service;

import br.com.alura.AluraFake.course.entities.Course;
import br.com.alura.AluraFake.course.CourseRepository;
import br.com.alura.AluraFake.course.enums.Status;
import br.com.alura.AluraFake.task.entities.MultipleChoiceTask;
import br.com.alura.AluraFake.task.entities.OpenTextTask;
import br.com.alura.AluraFake.task.TaskRepository;
import br.com.alura.AluraFake.task.dto.NewMultipleChoiceTaskDTO;
import br.com.alura.AluraFake.task.dto.OptionDTO;
import br.com.alura.AluraFake.task.dto.OpenTextTaskDTO;
import br.com.alura.AluraFake.task.service.impl.TaskServiceImpl;
import br.com.alura.AluraFake.task.validator.TaskValidator;
import br.com.alura.AluraFake.user.Role;
import br.com.alura.AluraFake.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private TaskValidator taskValidator;

    @Mock
    private TaskOrderService taskOrderService;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Course buildingCourse;
    private Course publishedCourse;
    private OpenTextTaskDTO validTaskDTO;
    private NewMultipleChoiceTaskDTO validMultipleChoiceDTO;

    @BeforeEach
    void setUp() {
        User instructor = new User("Paulo Silva", "paulo@alura.com.br", Role.INSTRUCTOR);
        buildingCourse = new Course("Java Fundamentals", "Learn Java basics", instructor);
        
        publishedCourse = new Course("Advanced Java", "Advanced Java concepts", instructor);
        publishedCourse.setStatus(Status.PUBLISHED);
        
        validTaskDTO = new OpenTextTaskDTO(1L, "What you think about Java?", 1);
        
        List<OptionDTO> validOptions = List.of(
                new OptionDTO("Java", true),
                new OptionDTO("Spring", true),
                new OptionDTO("Python", false)
        );
        validMultipleChoiceDTO = new NewMultipleChoiceTaskDTO(1L, "Which are Java technologies?", 1, validOptions);
    }

    @Test
    void createOpenTextTask__should_create_task_successfully() {
        // Given
        when(courseRepository.findById(1L)).thenReturn(Optional.of(buildingCourse));
        when(taskValidator.validateOpenTextTask(anyString(), anyInt(), any(Course.class)))
                .thenReturn(Collections.emptyList());
        when(taskOrderService.hasStatementConflict(anyLong(), anyString())).thenReturn(false);
        when(taskOrderService.isValidOrderPosition(anyLong(), anyInt())).thenReturn(true);
        
        OpenTextTask savedTask = new OpenTextTask("What you think about Java?", 1, buildingCourse);
        when(taskRepository.save(any(OpenTextTask.class))).thenReturn(savedTask);

        // When
        OpenTextTaskDTO result = taskService.createOpenTextTask(validTaskDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCourseId()).isEqualTo(1L);
        assertThat(result.getStatement()).isEqualTo("What you think about Java?");
        assertThat(result.getOrder()).isEqualTo(1);
        
        verify(courseRepository).findById(1L);
        verify(taskValidator).validateOpenTextTask("What you think about Java?", 1, buildingCourse);
        verify(taskOrderService).hasStatementConflict(1L, "What you think about Java?");
        verify(taskOrderService).isValidOrderPosition(1L, 1);
        verify(taskOrderService).handleOrderConflict(1L, 1);
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

    @Test
    void createOpenTextTask__should_throw_exception_when_course_is_published() {
        // Given
        when(courseRepository.findById(1L)).thenReturn(Optional.of(publishedCourse));
        when(taskValidator.validateOpenTextTask(anyString(), anyInt(), any(Course.class)))
                .thenReturn(List.of("Course must be in BUILDING status to receive tasks"));

        // When & Then
        assertThatThrownBy(() -> taskService.createOpenTextTask(validTaskDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Validation failed: Course must be in BUILDING status to receive tasks");
        
        verify(courseRepository).findById(1L);
        verify(taskValidator).validateOpenTextTask("What you think about Java?", 1, publishedCourse);
        verify(taskOrderService, never()).hasStatementConflict(anyLong(), anyString());
        verify(taskOrderService, never()).handleOrderConflict(anyLong(), anyInt());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void createOpenTextTask__should_throw_exception_when_statement_is_empty() {
        // Given
        OpenTextTaskDTO emptyStatementDTO = new OpenTextTaskDTO(1L, "", 1);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(buildingCourse));
        when(taskValidator.validateOpenTextTask(anyString(), anyInt(), any(Course.class)))
                .thenReturn(List.of("Statement cannot be empty"));

        // When & Then
        assertThatThrownBy(() -> taskService.createOpenTextTask(emptyStatementDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Validation failed: Statement cannot be empty");
        
        verify(courseRepository).findById(1L);
        verify(taskValidator).validateOpenTextTask("", 1, buildingCourse);
        verify(taskOrderService, never()).hasStatementConflict(anyLong(), anyString());
        verify(taskOrderService, never()).handleOrderConflict(anyLong(), anyInt());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void createOpenTextTask__should_throw_exception_when_order_position_is_zero() {
        // Given
        OpenTextTaskDTO zeroOrderDTO = new OpenTextTaskDTO(1L, "Valid statement", 0);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(buildingCourse));
        when(taskValidator.validateOpenTextTask(anyString(), anyInt(), any(Course.class)))
                .thenReturn(List.of("Order position must be positive"));

        // When & Then
        assertThatThrownBy(() -> taskService.createOpenTextTask(zeroOrderDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Validation failed: Order position must be positive");
        
        verify(courseRepository).findById(1L);
        verify(taskValidator).validateOpenTextTask("Valid statement", 0, buildingCourse);
        verify(taskRepository, never()).save(any());
    }

    @Test
    void createOpenTextTask__should_throw_exception_when_statement_conflict() {
        // Given
        when(courseRepository.findById(1L)).thenReturn(Optional.of(buildingCourse));
        when(taskValidator.validateOpenTextTask(anyString(), anyInt(), any(Course.class)))
                .thenReturn(Collections.emptyList());
        when(taskOrderService.hasStatementConflict(anyLong(), anyString())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> taskService.createOpenTextTask(validTaskDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A task with this statement already exists for this course");
        
        verify(courseRepository).findById(1L);
        verify(taskValidator).validateOpenTextTask("What you think about Java?", 1, buildingCourse);
        verify(taskOrderService).hasStatementConflict(1L, "What you think about Java?");
        verify(taskOrderService, never()).handleOrderConflict(anyLong(), anyInt());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void createOpenTextTask__should_throw_exception_when_order_position_invalid() {
        // Given
        OpenTextTaskDTO invalidOrderDTO = new OpenTextTaskDTO(1L, "Valid statement", 5); // Gap in sequence
        when(courseRepository.findById(1L)).thenReturn(Optional.of(buildingCourse));
        when(taskValidator.validateOpenTextTask(anyString(), anyInt(), any(Course.class)))
                .thenReturn(Collections.emptyList());
        when(taskOrderService.hasStatementConflict(anyLong(), anyString())).thenReturn(false);
        when(taskOrderService.isValidOrderPosition(anyLong(), anyInt())).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> taskService.createOpenTextTask(invalidOrderDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid order position. Order must be sequential starting from 1.");
        
        verify(courseRepository).findById(1L);
        verify(taskValidator).validateOpenTextTask("Valid statement", 5, buildingCourse);
        verify(taskOrderService).hasStatementConflict(1L, "Valid statement");
        verify(taskOrderService).isValidOrderPosition(1L, 5);
        verify(taskOrderService, never()).handleOrderConflict(anyLong(), anyInt());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void createMultipleChoiceTask__should_create_task_successfully() {
        // Given
        when(courseRepository.findById(1L)).thenReturn(Optional.of(buildingCourse));
        when(taskValidator.validateMultipleChoiceTask(anyString(), anyInt(), any(Course.class), any(List.class)))
                .thenReturn(Collections.emptyList());
        when(taskOrderService.hasStatementConflict(anyLong(), anyString())).thenReturn(false);
        when(taskOrderService.isValidOrderPosition(anyLong(), anyInt())).thenReturn(true);
        
        MultipleChoiceTask savedTask = new MultipleChoiceTask("Which are Java technologies?", 1, buildingCourse, Collections.emptyList());
        when(taskRepository.save(any(MultipleChoiceTask.class))).thenReturn(savedTask);

        // When
        NewMultipleChoiceTaskDTO result = taskService.createMultipleChoiceTask(validMultipleChoiceDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCourseId()).isEqualTo(1L);
        assertThat(result.getStatement()).isEqualTo("Which are Java technologies?");
        assertThat(result.getOrder()).isEqualTo(1);
        assertThat(result.getOptions()).hasSize(3);
        
        verify(courseRepository).findById(1L);
        verify(taskValidator).validateMultipleChoiceTask("Which are Java technologies?", 1, buildingCourse, validMultipleChoiceDTO.getOptions());
        verify(taskOrderService).hasStatementConflict(1L, "Which are Java technologies?");
        verify(taskOrderService).isValidOrderPosition(1L, 1);
        verify(taskOrderService).handleOrderConflict(1L, 1);
        verify(taskRepository).save(any(MultipleChoiceTask.class));
    }

    @Test
    void createMultipleChoiceTask__should_throw_exception_when_course_not_found() {
        // Given
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> taskService.createMultipleChoiceTask(validMultipleChoiceDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Course not found with id: 1");
        
        verify(courseRepository).findById(1L);
        verify(taskRepository, never()).save(any());
    }

    @Test
    void createMultipleChoiceTask__should_throw_exception_when_validation_fails() {
        // Given
        when(courseRepository.findById(1L)).thenReturn(Optional.of(buildingCourse));
        when(taskValidator.validateMultipleChoiceTask(anyString(), anyInt(), any(Course.class), any(List.class)))
                .thenReturn(List.of("MultipleChoice task must have at least two correct options"));

        // When & Then
        assertThatThrownBy(() -> taskService.createMultipleChoiceTask(validMultipleChoiceDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Validation failed: MultipleChoice task must have at least two correct options");
        
        verify(courseRepository).findById(1L);
        verify(taskValidator).validateMultipleChoiceTask("Which are Java technologies?", 1, buildingCourse, validMultipleChoiceDTO.getOptions());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void createMultipleChoiceTask__should_throw_exception_when_options_are_invalid() {
        // Given  
        List<OptionDTO> invalidOptions = List.of(
                new OptionDTO("Java", true),
                new OptionDTO("Java", false),
                new OptionDTO("Python", false)
        );
        NewMultipleChoiceTaskDTO invalidDTO = new NewMultipleChoiceTaskDTO(1L, "Which are Java technologies?", 1, invalidOptions);
        
        when(courseRepository.findById(1L)).thenReturn(Optional.of(buildingCourse));
        when(taskValidator.validateMultipleChoiceTask(anyString(), anyInt(), any(Course.class), any(List.class)))
                .thenReturn(List.of("Options cannot be equal to each other"));

        // When & Then
        assertThatThrownBy(() -> taskService.createMultipleChoiceTask(invalidDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Validation failed: Options cannot be equal to each other");
        
        verify(courseRepository).findById(1L);
        verify(taskValidator).validateMultipleChoiceTask("Which are Java technologies?", 1, buildingCourse, invalidOptions);
        verify(taskRepository, never()).save(any());
    }

    @Test
    void createMultipleChoiceTask__should_throw_exception_when_course_is_published() {
        // Given
        when(courseRepository.findById(1L)).thenReturn(Optional.of(publishedCourse));
        when(taskValidator.validateMultipleChoiceTask(anyString(), anyInt(), any(Course.class), any(List.class)))
                .thenReturn(List.of("Course must be in BUILDING status to receive tasks"));

        // When & Then
        assertThatThrownBy(() -> taskService.createMultipleChoiceTask(validMultipleChoiceDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Validation failed: Course must be in BUILDING status to receive tasks");
        
        verify(courseRepository).findById(1L);
        verify(taskValidator).validateMultipleChoiceTask("Which are Java technologies?", 1, publishedCourse, validMultipleChoiceDTO.getOptions());
        verify(taskRepository, never()).save(any());
    }

}