package br.com.alura.AluraFake.task.validator;

import br.com.alura.AluraFake.course.entities.Course;
import br.com.alura.AluraFake.course.enums.Status;

import java.util.ArrayList;
import java.util.List;

interface CommonTaskValidation {

    default List<String> validateCommon(String statement, Integer orderPosition, Course course) {
        List<String> errors = new ArrayList<>();

        if (statement == null || statement.trim().isEmpty()) {
            errors.add("Statement cannot be empty");
        }

        if (orderPosition == null || orderPosition <= 0) {
            errors.add("Order position must be positive");
        }

        if (course != null && !canTaskBeAddedToCourse(course)) {
            errors.add("Course must be in BUILDING status to receive tasks");
        }

        return errors;
    }

    default boolean canTaskBeAddedToCourse(Course course) {
        return course != null && course.getStatus() == Status.BUILDING;
    }
}
