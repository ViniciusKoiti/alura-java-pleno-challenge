package br.com.alura.AluraFake.course.validator;

import br.com.alura.AluraFake.user.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CourseCreationValidator {

    public List<String> validateInstructor(Optional<User> possibleInstructor) {
        List<String> errors = new ArrayList<>();

        if (possibleInstructor.isEmpty()) {
            errors.add("Instructor not found");
            return errors;
        }

        if (!possibleInstructor.get().isInstructor()) {
            errors.add("User is not an instructor");
        }

        return errors;
    }
}
