package br.com.alura.AluraFake.course.validator;

import br.com.alura.AluraFake.course.entities.Course;
import br.com.alura.AluraFake.course.enums.Status;
import br.com.alura.AluraFake.user.enums.Role;
import br.com.alura.AluraFake.user.entities.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StatusValidatorTest {

    private final StatusValidator validator = new StatusValidator();

    @Test
    void should_return_error_when_course_not_in_building() {
        User instructor = new User("Instructor", "instructor@example.com", Role.INSTRUCTOR, "password");
        Course course = new Course("Title", "Description", instructor);
        course.setStatus(Status.PUBLISHED);

        List<String> errors = validator.validate(course);

        assertThat(errors).contains("Course must be in BUILDING status to be published");
    }

    @Test
    void should_return_empty_when_course_in_building() {
        User instructor = new User("Instructor", "instructor@example.com", Role.INSTRUCTOR, "password");
        Course course = new Course("Title", "Description", instructor);

        List<String> errors = validator.validate(course);

        assertThat(errors).isEmpty();
    }
}
