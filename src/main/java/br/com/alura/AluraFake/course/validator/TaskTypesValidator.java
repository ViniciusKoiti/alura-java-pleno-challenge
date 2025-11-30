package br.com.alura.AluraFake.course.validator;

import br.com.alura.AluraFake.task.TaskRepository;
import br.com.alura.AluraFake.task.entities.Task;
import br.com.alura.AluraFake.task.enums.Type;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TaskTypesValidator implements CommonPublicationValidation {

    private final TaskRepository taskRepository;

    public TaskTypesValidator(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<String> validate(Long courseId) {
        List<Task> tasks = taskRepository.findByCourseIdOrderByOrderPositionAsc(courseId);
        Set<Type> existingTypes = tasks.stream()
                .map(Task::getType)
                .collect(Collectors.toSet());

        List<String> errors = new ArrayList<>();

        if (!existingTypes.contains(Type.OPEN_TEXT)) {
            errors.add("Course must have at least one OPEN_TEXT task");
        }
        if (!existingTypes.contains(Type.SINGLE_CHOICE)) {
            errors.add("Course must have at least one SINGLE_CHOICE task");
        }
        if (!existingTypes.contains(Type.MULTIPLE_CHOICE)) {
            errors.add("Course must have at least one MULTIPLE_CHOICE task");
        }

        return errors;
    }
}
