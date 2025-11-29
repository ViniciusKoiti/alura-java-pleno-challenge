package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.course.Status;
import br.com.alura.AluraFake.user.Role;
import br.com.alura.AluraFake.user.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OpenTextTaskTest {

    @Test
    void constructor__should_create_open_text_task() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        
        OpenTextTask task = new OpenTextTask("Explain polymorphism in Java", 1, course);
        
        assertThat(task.getStatement()).isEqualTo("Explain polymorphism in Java");
        assertThat(task.getOrderPosition()).isEqualTo(1);
        assertThat(task.getCourse()).isEqualTo(course);
        assertThat(task.getType()).isEqualTo(Type.OPEN_TEXT);
        assertThat(task.getCreatedAt()).isNotNull();
    }

    @Test
    void validate__should_return_no_errors_for_valid_task() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        
        OpenTextTask task = new OpenTextTask("What are the main principles of OOP?", 1, course);
        
        List<String> errors = task.validate();
        
        assertThat(errors).isEmpty();
    }

    @Test
    void validate__should_return_errors_for_invalid_statement() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        
        assertThatThrownBy(() -> new OpenTextTask(null, 1, course))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Statement cannot be null or empty");
        
        assertThatThrownBy(() -> new OpenTextTask("", 1, course))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Statement cannot be null or empty");
        
        assertThatThrownBy(() -> new OpenTextTask("   ", 1, course))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Statement cannot be null or empty");
    }

    @Test
    void validate__should_return_errors_for_invalid_order() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        
        assertThatThrownBy(() -> new OpenTextTask("Valid statement", null, course))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Order position cannot be null");
        
        assertThatThrownBy(() -> new OpenTextTask("Valid statement", 0, course))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Order position must be positive");
        
        assertThatThrownBy(() -> new OpenTextTask("Valid statement", -1, course))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Order position must be positive");
    }

    @Test
    void validate__should_return_errors_when_course_not_in_building() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        course.setStatus(Status.PUBLISHED);
        
        OpenTextTask task = new OpenTextTask("What is inheritance?", 1, course);
        
        List<String> errors = task.validate();
        
        assertThat(errors).contains("Course must be in BUILDING status to receive tasks");
    }

    @Test
    void getType__should_return_open_text_type() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        
        OpenTextTask task = new OpenTextTask("Describe abstraction", 1, course);
        
        assertThat(task.getType()).isEqualTo(Type.OPEN_TEXT);
    }

    @Test
    void canBeAddedToCourse__should_return_true_when_course_building() {
        // TODO: Implement test for course status check
    }

    @Test
    void canBeAddedToCourse__should_return_false_when_course_published() {
        // TODO: Implement test for published course check
    }
}