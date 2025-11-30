package br.com.alura.AluraFake.task.validator;

import br.com.alura.AluraFake.course.entities.Course;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OpenTextTaskValidator implements CommonTaskValidation {

    public List<String> validate(String statement, Integer orderPosition, Course course) {
        return validateCommon(statement, orderPosition, course);
    }

    public boolean canTaskBeAddedToCourse(Course course) {
        return CommonTaskValidation.super.canTaskBeAddedToCourse(course);
    }
}
