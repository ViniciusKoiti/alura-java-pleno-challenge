package br.com.alura.AluraFake.course.validator;

import br.com.alura.AluraFake.course.Course;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CoursePublicationValidator {

    private final StatusValidator statusValidator;
    private final TaskTypesValidator taskTypesValidator;
    private final SequenceValidator sequenceValidator;

    public CoursePublicationValidator(StatusValidator statusValidator,
                                      TaskTypesValidator taskTypesValidator,
                                      SequenceValidator sequenceValidator) {
        this.statusValidator = statusValidator;
        this.taskTypesValidator = taskTypesValidator;
        this.sequenceValidator = sequenceValidator;
    }

    public List<String> validatePublication(Course course, Long courseId) {
        List<String> errors = new ArrayList<>();

        errors.addAll(statusValidator.validate(course));
        errors.addAll(taskTypesValidator.validate(courseId));
        errors.addAll(sequenceValidator.validate(courseId));

        return errors;
    }
}
