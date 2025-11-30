package br.com.alura.AluraFake.task.service.impl;


import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.course.CourseRepository;
import br.com.alura.AluraFake.task.MultipleChoiceTask;
import br.com.alura.AluraFake.task.OpenTextTask;
import br.com.alura.AluraFake.task.SingleChoiceTask;
import br.com.alura.AluraFake.task.TaskOption;
import br.com.alura.AluraFake.task.TaskRepository;
import br.com.alura.AluraFake.task.dto.NewMultipleChoiceTaskDTO;
import br.com.alura.AluraFake.task.dto.NewSingleChoiceTaskDTO;
import br.com.alura.AluraFake.task.dto.OptionDTO;
import br.com.alura.AluraFake.task.dto.OpenTextTaskDTO;
import br.com.alura.AluraFake.task.service.TaskService;
import br.com.alura.AluraFake.task.validator.TaskValidator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
        private final TaskRepository taskRepository;
        private final CourseRepository courseRepository;
        private final TaskValidator taskValidator;

    public TaskServiceImpl(TaskRepository taskRepository, CourseRepository courseRepository, TaskValidator taskValidator) {
        this.taskRepository = taskRepository;
        this.courseRepository = courseRepository;
        this.taskValidator = taskValidator;
    }
    @Override
    @Transactional
    public OpenTextTaskDTO createOpenTextTask(OpenTextTaskDTO dto) {
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + dto.getCourseId()));

        List<String> errors = taskValidator.validateOpenTextTask(dto.getStatement(), dto.getOrder(), course);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Validation failed: " + String.join(", ", errors));
        }

        OpenTextTask task = new OpenTextTask(
                dto.getStatement(),
                dto.getOrder(),
                course
        );
        OpenTextTask saved = taskRepository.save(task);
        return new OpenTextTaskDTO(
                dto.getCourseId(),
                saved.getStatement(),
                saved.getOrderPosition()
        );
    }

    @Override
    @Transactional
    public NewSingleChoiceTaskDTO createSingleChoiceTask(NewSingleChoiceTaskDTO dto) {
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + dto.getCourseId()));

        List<String> errors = taskValidator.validateSingleChoiceTask(
                dto.getStatement(), 
                dto.getOrder(), 
                course, 
                dto.getOptions()
        );
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Validation failed: " + String.join(", ", errors));
        }

        List<TaskOption> options = new ArrayList<>();
        for (int i = 0; i < dto.getOptions().size(); i++) {
            OptionDTO optionDto = dto.getOptions().get(i);
            TaskOption option = new TaskOption(
                    optionDto.getOption(),
                    optionDto.getIsCorrect(),
                    i + 1,
                    null
            );
            options.add(option);
        }

        SingleChoiceTask task = new SingleChoiceTask(
                dto.getStatement(),
                dto.getOrder(),
                course,
                options
        );

        SingleChoiceTask saved = taskRepository.save(task);
        return new NewSingleChoiceTaskDTO(
                dto.getCourseId(),
                saved.getStatement(),
                saved.getOrderPosition(),
                dto.getOptions()
        );
    }

    @Override
    @Transactional
    public NewMultipleChoiceTaskDTO createMultipleChoiceTask(NewMultipleChoiceTaskDTO dto) {
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + dto.getCourseId()));

        List<String> errors = taskValidator.validateMultipleChoiceTask(
                dto.getStatement(), 
                dto.getOrder(), 
                course, 
                dto.getOptions()
        );
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Validation failed: " + String.join(", ", errors));
        }

        List<TaskOption> options = new ArrayList<>();
        for (int i = 0; i < dto.getOptions().size(); i++) {
            OptionDTO optionDto = dto.getOptions().get(i);
            TaskOption option = new TaskOption(
                    optionDto.getOption(),
                    optionDto.getIsCorrect(),
                    i + 1,
                    null
            );
            options.add(option);
        }

        MultipleChoiceTask task = new MultipleChoiceTask(
                dto.getStatement(),
                dto.getOrder(),
                course,
                options
        );

        MultipleChoiceTask saved = taskRepository.save(task);
        return new NewMultipleChoiceTaskDTO(
                dto.getCourseId(),
                saved.getStatement(),
                saved.getOrderPosition(),
                dto.getOptions()
        );
    }
}
