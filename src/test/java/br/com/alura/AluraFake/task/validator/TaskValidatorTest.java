package br.com.alura.AluraFake.task.validator;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.course.Status;
import br.com.alura.AluraFake.task.dto.OptionDTO;
import br.com.alura.AluraFake.user.Role;
import br.com.alura.AluraFake.user.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TaskValidatorTest {
    
    private final TaskValidator validator = new TaskValidator();

    @Test
    void validateMultipleChoiceTask__should_pass_with_valid_input() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        course.setStatus(Status.BUILDING);
        
        List<OptionDTO> options = List.of(
                new OptionDTO("Java", true),
                new OptionDTO("Spring", true),
                new OptionDTO("Python", false)
        );
        
        List<String> errors = validator.validateMultipleChoiceTask(
                "Which are Java technologies?", 1, course, options
        );
        
        assertThat(errors).isEmpty();
    }

    @Test
    void validateMultipleChoiceTask__should_fail_with_less_than_3_options() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        course.setStatus(Status.BUILDING);
        
        List<OptionDTO> options = List.of(
                new OptionDTO("Java", true),
                new OptionDTO("Python", false)
        );
        
        List<String> errors = validator.validateMultipleChoiceTask(
                "Which are Java technologies?", 1, course, options
        );
        
        assertThat(errors).contains("MultipleChoice task must have between 3 and 5 options");
    }

    @Test
    void validateMultipleChoiceTask__should_fail_with_more_than_5_options() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        course.setStatus(Status.BUILDING);
        
        List<OptionDTO> options = List.of(
                new OptionDTO("Java", true),
                new OptionDTO("Spring", true),
                new OptionDTO("Python", false),
                new OptionDTO("C++", false),
                new OptionDTO("JavaScript", false),
                new OptionDTO("Go", false)
        );
        
        List<String> errors = validator.validateMultipleChoiceTask(
                "Which are Java technologies?", 1, course, options
        );
        
        assertThat(errors).contains("MultipleChoice task must have between 3 and 5 options");
    }

    @Test
    void validateMultipleChoiceTask__should_fail_with_less_than_2_correct_options() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        course.setStatus(Status.BUILDING);
        
        List<OptionDTO> options = List.of(
                new OptionDTO("Java", true),
                new OptionDTO("Spring", false),
                new OptionDTO("Python", false)
        );
        
        List<String> errors = validator.validateMultipleChoiceTask(
                "Which are Java technologies?", 1, course, options
        );
        
        assertThat(errors).contains("MultipleChoice task must have at least two correct options");
    }

    @Test
    void validateMultipleChoiceTask__should_fail_with_no_incorrect_options() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        course.setStatus(Status.BUILDING);
        
        List<OptionDTO> options = List.of(
                new OptionDTO("Java", true),
                new OptionDTO("Spring", true),
                new OptionDTO("Hibernate", true)
        );
        
        List<String> errors = validator.validateMultipleChoiceTask(
                "Which are Java technologies?", 1, course, options
        );
        
        assertThat(errors).contains("MultipleChoice task must have at least one incorrect option");
    }

    @Test
    void validateMultipleChoiceTask__should_fail_with_duplicate_options() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        course.setStatus(Status.BUILDING);
        
        List<OptionDTO> options = List.of(
                new OptionDTO("Java", true),
                new OptionDTO("Spring", true),
                new OptionDTO("Java", false)
        );
        
        List<String> errors = validator.validateMultipleChoiceTask(
                "Which are Java technologies?", 1, course, options
        );
        
        assertThat(errors).contains("Options cannot be equal to each other");
    }

    @Test
    void validateMultipleChoiceTask__should_fail_when_option_equals_statement() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        course.setStatus(Status.BUILDING);
        
        List<OptionDTO> options = List.of(
                new OptionDTO("Which are Java technologies?", true),
                new OptionDTO("Spring", true),
                new OptionDTO("Python", false)
        );
        
        List<String> errors = validator.validateMultipleChoiceTask(
                "Which are Java technologies?", 1, course, options
        );
        
        assertThat(errors).contains("Option cannot be equal to the statement");
    }

    @Test
    void validateMultipleChoiceTask__should_fail_with_empty_statement() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        course.setStatus(Status.BUILDING);
        
        List<OptionDTO> options = List.of(
                new OptionDTO("Java", true),
                new OptionDTO("Spring", true),
                new OptionDTO("Python", false)
        );
        
        List<String> errors = validator.validateMultipleChoiceTask(
                "", 1, course, options
        );
        
        assertThat(errors).contains("Statement cannot be empty");
    }

    @Test
    void validateMultipleChoiceTask__should_fail_with_negative_order() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        course.setStatus(Status.BUILDING);
        
        List<OptionDTO> options = List.of(
                new OptionDTO("Java", true),
                new OptionDTO("Spring", true),
                new OptionDTO("Python", false)
        );
        
        List<String> errors = validator.validateMultipleChoiceTask(
                "Which are Java technologies?", -1, course, options
        );
        
        assertThat(errors).contains("Order position must be positive");
    }

    @Test
    void validateMultipleChoiceTask__should_fail_with_published_course() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        course.setStatus(Status.PUBLISHED);
        
        List<OptionDTO> options = List.of(
                new OptionDTO("Java", true),
                new OptionDTO("Spring", true),
                new OptionDTO("Python", false)
        );
        
        List<String> errors = validator.validateMultipleChoiceTask(
                "Which are Java technologies?", 1, course, options
        );
        
        assertThat(errors).contains("Course must be in BUILDING status to receive tasks");
    }
}