package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.course.entities.Course;
import br.com.alura.AluraFake.task.entities.MultipleChoiceTask;
import br.com.alura.AluraFake.task.entities.TaskOption;
import br.com.alura.AluraFake.task.enums.Type;
import br.com.alura.AluraFake.user.Role;
import br.com.alura.AluraFake.user.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MultipleChoiceTaskTest {

    @Test
    void constructor__should_create_multiple_choice_task() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        List<TaskOption> options = List.of(
                new TaskOption("Java", true, 1, null),
                new TaskOption("Spring", true, 2, null),
                new TaskOption("Python", false, 3, null)
        );
        
        MultipleChoiceTask task = new MultipleChoiceTask("Which technologies are related to Java ecosystem?", 1, course, options);
        
        assertThat(task.getStatement()).isEqualTo("Which technologies are related to Java ecosystem?");
        assertThat(task.getOrderPosition()).isEqualTo(1);
        assertThat(task.getCourse()).isEqualTo(course);
        assertThat(task.getType()).isEqualTo(Type.MULTIPLE_CHOICE);
        assertThat(task.getCreatedAt()).isNotNull();
        assertThat(task.getOptions()).hasSize(3);
    }

    @Test
    void constructor__should_allow_creation_with_any_values() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        List<TaskOption> options = List.of(
                new TaskOption("OOP", true, 1, null),
                new TaskOption("Encapsulation", true, 2, null),
                new TaskOption("Inheritance", true, 3, null),
                new TaskOption("Assembly", false, 4, null)
        );
        
        MultipleChoiceTask task = new MultipleChoiceTask("What are the main principles of OOP?", 2, course, options);
        
        assertThat(task.getStatement()).isEqualTo("What are the main principles of OOP?");
        assertThat(task.getOrderPosition()).isEqualTo(2);
        assertThat(task.getCourse()).isEqualTo(course);
        assertThat(task.getOptions()).hasSize(4);
    }

    @Test
    void constructor__should_set_task_reference_in_options() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        List<TaskOption> options = List.of(
                new TaskOption("Spring Boot", true, 1, null),
                new TaskOption("Spring MVC", true, 2, null),
                new TaskOption("Django", false, 3, null)
        );
        
        MultipleChoiceTask task = new MultipleChoiceTask("Which are Spring frameworks?", 1, course, options);
        
        assertThat(task.getOptions()).allSatisfy(option -> 
            assertThat(option.getTask()).isEqualTo(task)
        );
    }

    @Test
    void getType__should_return_multiple_choice_type() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        List<TaskOption> options = List.of(
                new TaskOption("Java", true, 1, null),
                new TaskOption("Kotlin", true, 2, null),
                new TaskOption("C++", false, 3, null)
        );
        
        MultipleChoiceTask task = new MultipleChoiceTask("Which run on JVM?", 1, course, options);
        
        assertThat(task.getType()).isEqualTo(Type.MULTIPLE_CHOICE);
    }

    @Test
    void addOption__should_add_option_with_task_reference() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        List<TaskOption> initialOptions = List.of(
                new TaskOption("MySQL", true, 1, null),
                new TaskOption("PostgreSQL", true, 2, null),
                new TaskOption("Excel", false, 3, null)
        );
        
        MultipleChoiceTask task = new MultipleChoiceTask("Which are databases?", 1, course, initialOptions);
        TaskOption newOption = new TaskOption("MongoDB", true, 4, null);
        
        task.addOption(newOption);
        
        assertThat(task.getOptions()).hasSize(4);
        assertThat(newOption.getTask()).isEqualTo(task);
    }

    @Test
    void constructor__should_allow_null_values() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        
        MultipleChoiceTask task = new MultipleChoiceTask(null, null, course, null);
        
        assertThat(task.getStatement()).isNull();
        assertThat(task.getOrderPosition()).isNull();
        assertThat(task.getCourse()).isEqualTo(course);
        assertThat(task.getOptions()).isEmpty();
    }

    @Test
    void constructor__should_handle_empty_options_list() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        List<TaskOption> emptyOptions = List.of();
        
        MultipleChoiceTask task = new MultipleChoiceTask("Empty options test", 1, course, emptyOptions);
        
        assertThat(task.getStatement()).isEqualTo("Empty options test");
        assertThat(task.getOrderPosition()).isEqualTo(1);
        assertThat(task.getCourse()).isEqualTo(course);
        assertThat(task.getOptions()).isEmpty();
    }
}