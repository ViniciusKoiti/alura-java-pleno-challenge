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
import br.com.alura.AluraFake.task.service.TaskOrderService;
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
        private final TaskOrderService taskOrderService;

    public TaskServiceImpl(TaskRepository taskRepository, CourseRepository courseRepository, TaskValidator taskValidator, TaskOrderService taskOrderService) {
        this.taskRepository = taskRepository;
        this.courseRepository = courseRepository;
        this.taskValidator = taskValidator;
        this.taskOrderService = taskOrderService;
    }
    @Override
    @Transactional
    public OpenTextTaskDTO createOpenTextTask(OpenTextTaskDTO dto) {
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + dto.getCourseId()));

        List<String> errors = taskValidator.validateOpenTextTask(dto.getStatement(), dto.getOrder(), course);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(formatValidationErrors(errors));
        }

        if (taskOrderService.hasStatementConflict(dto.getCourseId(), dto.getStatement())) {
            throw new IllegalArgumentException("A task with this statement already exists for this course");
        }

        if (!taskOrderService.isValidOrderPosition(dto.getCourseId(), dto.getOrder())) {
            throw new IllegalArgumentException("Invalid order position. Order must be sequential starting from 1.");
        }

        taskOrderService.handleOrderConflict(dto.getCourseId(), dto.getOrder());

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
            throw new IllegalArgumentException(formatValidationErrors(errors));
        }

        if (taskOrderService.hasStatementConflict(dto.getCourseId(), dto.getStatement())) {
            throw new IllegalArgumentException("A task with this statement already exists for this course");
        }

        if (!taskOrderService.isValidOrderPosition(dto.getCourseId(), dto.getOrder())) {
            throw new IllegalArgumentException("Invalid order position. Order must be sequential starting from 1.");
        }

        taskOrderService.handleOrderConflict(dto.getCourseId(), dto.getOrder());

        List<TaskOption> options = mapOptions(dto.getOptions());

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
            throw new IllegalArgumentException(formatValidationErrors(errors));
        }

        if (taskOrderService.hasStatementConflict(dto.getCourseId(), dto.getStatement())) {
            throw new IllegalArgumentException("A task with this statement already exists for this course");
        }

        if (!taskOrderService.isValidOrderPosition(dto.getCourseId(), dto.getOrder())) {
            throw new IllegalArgumentException("Invalid order position. Order must be sequential starting from 1.");
        }

        taskOrderService.handleOrderConflict(dto.getCourseId(), dto.getOrder());

        List<TaskOption> options = mapOptions(dto.getOptions());

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

    private List<TaskOption> mapOptions(List<OptionDTO> optionDTOs) {
        List<TaskOption> options = new ArrayList<>();
        for (int i = 0; i < optionDTOs.size(); i++) {
            OptionDTO optionDto = optionDTOs.get(i);
            TaskOption option = new TaskOption(
                    optionDto.getOption(),
                    optionDto.getIsCorrect(),
                    i + 1,
                    null
            );
            options.add(option);
        }
        return options;
    }

    private String formatValidationErrors(List<String> errors) {
        return "Validation failed: " + String.join(", ", errors);
    }
}
