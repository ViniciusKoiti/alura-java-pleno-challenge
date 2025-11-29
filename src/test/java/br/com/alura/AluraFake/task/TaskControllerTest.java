package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.task.dto.OpenTextTaskDTO;
import br.com.alura.AluraFake.task.service.TaskService;
import br.com.alura.AluraFake.user.Role;
import br.com.alura.AluraFake.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void newOpenTextExercise__should_create_open_text_task_successfully() throws Exception {
        User instructor = new User("Paulo Silva", "paulo@alura.com.br", Role.INSTRUCTOR);
        Course course = new Course("Java Fundamentals", "Learn Java basics", instructor);

        OpenTextTaskDTO openTextTaskDTO = new OpenTextTaskDTO(1L, "What you think about statement?", 1);

        mockMvc.perform(post("/task/new/opentext")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(openTextTaskDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void newOpenTextExercise__should_return_error_when_the_statement_size_is_less_than_4() throws Exception {
        OpenTextTaskDTO openTextTaskDTO = new OpenTextTaskDTO(1L, "Wha", 1);

        mockMvc.perform(post("/task/new/opentext")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(openTextTaskDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("statement"))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

    @Test
    void newOpenTextExercise__should_return_error_when_the_statement_size_is_greater_than_256() throws Exception {
        OpenTextTaskDTO openTextTaskDTO = new OpenTextTaskDTO(1L, """
                REST is an architectural style defined by Roy Fielding around 2000.
                It emphasizes stateless communication, uniform interfaces, and resources identified by URIs, shaping how modern web APIs are designed,
                 built, and understood worldwide, how that impact in your develop?
                """, 2);

        mockMvc.perform(post("/task/new/opentext")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(openTextTaskDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("statement"))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

    @Test
    void newOpenTextExercise__should_return_error_when_courseId_is_null() throws Exception {
        OpenTextTaskDTO openTextTaskDTO = new OpenTextTaskDTO(null, "Valid statement here", 1);

        mockMvc.perform(post("/task/new/opentext")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(openTextTaskDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[?(@.field == 'courseId')]").exists());
    }

}

