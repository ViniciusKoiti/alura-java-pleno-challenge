package br.com.alura.AluraFake.course.validator;

import br.com.alura.AluraFake.course.entities.Course;

import java.util.ArrayList;
import java.util.List;

interface CommonPublicationValidation {

    default List<String> validateCommon(Course course) {
        List<String> errors = new ArrayList<>();

        if (course == null) {
            errors.add("Course not found");
        }

        return errors;
    }
}
