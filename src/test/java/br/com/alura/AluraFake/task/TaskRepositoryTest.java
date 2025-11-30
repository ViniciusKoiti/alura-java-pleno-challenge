package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.course.CourseRepository;
import br.com.alura.AluraFake.user.Role;
import br.com.alura.AluraFake.user.User;
import br.com.alura.AluraFake.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    private Course course;

    @BeforeEach
    void setUp() {
        User instructor = new User("Professor Java", "professor@alura.com.br", Role.INSTRUCTOR);
        userRepository.save(instructor);
        
        course = new Course("Java Fundamentals", "Learn Java basics", instructor);
        courseRepository.save(course);
    }

    @Test
    void findByCourseIdAndOrderPosition__should_return_task_when_exists() {
        // Given
        OpenTextTask task = new OpenTextTask("What is Java?", 1, course);
        taskRepository.save(task);

        // When
        Optional<Task> result = taskRepository.findByCourseIdAndOrderPosition(course.getId(), 1);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getStatement()).isEqualTo("What is Java?");
        assertThat(result.get().getOrderPosition()).isEqualTo(1);
    }


    @Test
    void findByCourseIdAndOrderPosition__should_return_empty_when_not_exists() {
        // Given
        OpenTextTask task = new OpenTextTask("What is Java?", 1, course);
        taskRepository.save(task);

        // When
        Optional<Task> result = taskRepository.findByCourseIdAndOrderPosition(course.getId(), 2);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findByCourseIdOrderByOrderPositionAsc__should_return_tasks_ordered() {
        // Given
        OpenTextTask task1 = new OpenTextTask("Question 1", 3, course);
        OpenTextTask task2 = new OpenTextTask("Question 2", 1, course);
        OpenTextTask task3 = new OpenTextTask("Question 3", 2, course);

        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        // When
        List<Task> result = taskRepository.findByCourseIdOrderByOrderPositionAsc(course.getId());

        // Then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getOrderPosition()).isEqualTo(1);
        assertThat(result.get(1).getOrderPosition()).isEqualTo(2);
        assertThat(result.get(2).getOrderPosition()).isEqualTo(3);
        assertThat(result.get(0).getStatement()).isEqualTo("Question 2");
        assertThat(result.get(1).getStatement()).isEqualTo("Question 3");
        assertThat(result.get(2).getStatement()).isEqualTo("Question 1");
    }

    @Test
    void findByCourseIdOrderByOrderPositionAsc__should_return_empty_list_when_no_tasks() {
        // When
        List<Task> result = taskRepository.findByCourseIdOrderByOrderPositionAsc(course.getId());

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void existsByCourseIdAndStatement__should_return_true_when_statement_exists() {
        // Given
        String statement = "What is polymorphism?";
        OpenTextTask task = new OpenTextTask(statement, 1, course);
        taskRepository.save(task);

        // When
        boolean result = taskRepository.existsByCourseIdAndStatement(course.getId(), statement);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void existsByCourseIdAndStatement__should_return_false_when_statement_not_exists() {
        // Given
        OpenTextTask task = new OpenTextTask("What is polymorphism?", 1, course);
        taskRepository.save(task);

        // When
        boolean result = taskRepository.existsByCourseIdAndStatement(course.getId(), "What is inheritance?");

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void incrementOrderPositionsFromOrder__should_execute_without_error() {
        // Given
        OpenTextTask task1 = new OpenTextTask("Question 1", 1, course);
        OpenTextTask task2 = new OpenTextTask("Question 2", 5, course);

        taskRepository.save(task1);
        taskRepository.save(task2);

        taskRepository.incrementOrderPositionsFromOrder(course.getId(), 10);

        // Then
        List<Task> tasks = taskRepository.findByCourseIdOrderByOrderPositionAsc(course.getId());
        assertThat(tasks).hasSize(2);
    }

    @Test
    void isOrderSequenceContinuous__should_return_true_for_continuous_sequence() {
        // Given
        OpenTextTask task1 = new OpenTextTask("Question 1", 1, course);
        OpenTextTask task2 = new OpenTextTask("Question 2", 2, course);
        OpenTextTask task3 = new OpenTextTask("Question 3", 3, course);

        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        // When
        boolean result = taskRepository.isOrderSequenceContinuous(course.getId());

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void isOrderSequenceContinuous__should_return_false_for_gap_in_sequence() {
        // Given
        OpenTextTask task1 = new OpenTextTask("Question 1", 1, course);
        OpenTextTask task2 = new OpenTextTask("Question 2", 3, course);
        OpenTextTask task3 = new OpenTextTask("Question 3", 4, course);

        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        // When
        boolean result = taskRepository.isOrderSequenceContinuous(course.getId());

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void isOrderSequenceContinuous__should_return_false_when_not_starting_from_one() {
        // Given
        OpenTextTask task1 = new OpenTextTask("Question 1", 2, course);
        OpenTextTask task2 = new OpenTextTask("Question 2", 3, course);

        taskRepository.save(task1);
        taskRepository.save(task2);

        // When
        boolean result = taskRepository.isOrderSequenceContinuous(course.getId());

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void isOrderSequenceContinuous__should_return_true_when_no_tasks() {
        // When
        boolean result = taskRepository.isOrderSequenceContinuous(course.getId());

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void isOrderSequenceContinuous__should_return_true_for_single_task_with_order_one() {
        // Given
        OpenTextTask task = new OpenTextTask("Single question", 1, course);
        taskRepository.save(task);

        // When
        boolean result = taskRepository.isOrderSequenceContinuous(course.getId());

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void isOrderSequenceContinuous__should_return_false_for_single_task_not_starting_from_one() {
        // Given
        OpenTextTask task = new OpenTextTask("Single question", 5, course);
        taskRepository.save(task);

        // When
        boolean result = taskRepository.isOrderSequenceContinuous(course.getId());

        // Then
        assertThat(result).isFalse();
    }

}