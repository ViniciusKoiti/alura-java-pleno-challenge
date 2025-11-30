package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.course.entities.Course;
import br.com.alura.AluraFake.task.entities.OpenTextTask;
import br.com.alura.AluraFake.task.enums.Type;
import br.com.alura.AluraFake.user.enums.Role;
import br.com.alura.AluraFake.user.entities.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
    void constructor__should_allow_creation_with_any_values() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        
        OpenTextTask task = new OpenTextTask("What are the main principles of OOP?", 1, course);
        
        assertThat(task.getStatement()).isEqualTo("What are the main principles of OOP?");
        assertThat(task.getOrderPosition()).isEqualTo(1);
        assertThat(task.getCourse()).isEqualTo(course);
    }

    @Test
    void constructor__should_allow_empty_statement() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        
        OpenTextTask task = new OpenTextTask("", 0, course);
        
        assertThat(task.getStatement()).isEqualTo("");
        assertThat(task.getOrderPosition()).isEqualTo(0);
        assertThat(task.getCourse()).isEqualTo(course);
    }

    @Test
    void constructor__should_allow_null_values() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        
        OpenTextTask task = new OpenTextTask(null, null, course);
        
        assertThat(task.getStatement()).isNull();
        assertThat(task.getOrderPosition()).isNull();
        assertThat(task.getCourse()).isEqualTo(course);
    }

    @Test
    void getType__should_return_open_text_type() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java", "Curso de Java", instructor);
        
        OpenTextTask task = new OpenTextTask("Describe abstraction", 1, course);
        
        assertThat(task.getType()).isEqualTo(Type.OPEN_TEXT);
    }
}