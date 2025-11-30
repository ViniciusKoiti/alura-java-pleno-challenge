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
    private User instructor;

    @BeforeEach
    void setUp() {
        instructor = new User("Professor Java", "professor@alura.com.br", Role.INSTRUCTOR);
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

}