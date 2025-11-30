package br.com.alura.AluraFake.course.validator;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.course.Status;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StatusValidator implements CommonPublicationValidation {

    public List<String> validate(Course course) {
        List<String> errors = new ArrayList<>(validateCommon(course));

        if (course != null && course.getStatus() != Status.BUILDING) {
            errors.add("Course must be in BUILDING status to be published");
        }

        return errors;
    }
}
