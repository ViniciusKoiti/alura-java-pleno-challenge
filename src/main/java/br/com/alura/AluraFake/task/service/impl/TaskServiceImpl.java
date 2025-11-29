package br.com.alura.AluraFake.task.service.impl;


import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.course.CourseRepository;
import br.com.alura.AluraFake.task.OpenTextTask;
import br.com.alura.AluraFake.task.TaskRepository;
import br.com.alura.AluraFake.task.dto.OpenTextTaskDTO;
import br.com.alura.AluraFake.task.service.TaskService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
        private final TaskRepository taskRepository;
        private final CourseRepository courseRepository;

    public TaskServiceImpl(TaskRepository taskRepository, CourseRepository courseRepository) {
        this.taskRepository = taskRepository;
        this.courseRepository = courseRepository;
    }
    @Override
    @Transactional
    public OpenTextTaskDTO createOpenTextTask(OpenTextTaskDTO dto) {
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + dto.getCourseId()));

        OpenTextTask task = new OpenTextTask(
                dto.getStatement(),
                dto.getOrder(),
                course
        );
        List<String> errors = task.validate();
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Validation failed: " + String.join(", ", errors));
        }
        OpenTextTask saved = taskRepository.save(task);
        return new OpenTextTaskDTO(
                dto.getCourseId(),
                saved.getStatement(),
                saved.getOrderPosition()
        );
    }
}
