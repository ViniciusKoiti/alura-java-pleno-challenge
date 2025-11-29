package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.course.CourseRepository;
import br.com.alura.AluraFake.user.Role;
import br.com.alura.AluraFake.user.User;
import br.com.alura.AluraFake.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    void newOpenTextExercise__should_create_open_text_task_successfully() throws Exception {
        User instructor = new User("Paulo Silva", "paulo@alura.com.br", Role.INSTRUCTOR);
        userRepository.save(instructor);
        
        Course course = new Course("Java Fundamentals", "Learn Java basics", instructor);


        Long courseId = 1L;

        String requestBody = """
            {
                "courseId": %d,
                "statement": "What are the main principles of Object-Oriented Programming?",
                "order": 1
            }
            """.formatted(courseId);

        mockMvc.perform(post("/task/new/opentext")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());
    }
}