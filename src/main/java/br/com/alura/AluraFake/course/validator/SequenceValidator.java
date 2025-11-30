package br.com.alura.AluraFake.course.validator;

import br.com.alura.AluraFake.task.service.TaskOrderService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SequenceValidator implements CommonPublicationValidation {

    private final TaskOrderService taskOrderService;

    public SequenceValidator(TaskOrderService taskOrderService) {
        this.taskOrderService = taskOrderService;
    }

    public List<String> validate(Long courseId) {
        List<String> errors = new ArrayList<>();

        if (!taskOrderService.isSequenceContinuous(courseId)) {
            errors.add("Tasks must have continuous order sequence starting from 1");
        }

        return errors;
    }
}
