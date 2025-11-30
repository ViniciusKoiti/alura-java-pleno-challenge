package br.com.alura.AluraFake.task.validator;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.task.dto.OptionDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SingleChoiceTaskValidator implements CommonTaskValidation {

    public List<String> validate(String statement, Integer orderPosition, Course course, List<OptionDTO> options) {
        List<String> errors = new ArrayList<>(validateCommon(statement, orderPosition, course));

        if (options == null || options.size() < 2 || options.size() > 5) {
            errors.add("SingleChoice task must have between 2 and 5 options");
        } else {
            errors.addAll(validateOptions(statement, options));
        }

        return errors;
    }

    private List<String> validateOptions(String statement, List<OptionDTO> options) {
        List<String> errors = new ArrayList<>();

        long correctCount = options.stream()
                .filter(option -> option.getIsCorrect() != null && option.getIsCorrect())
                .count();

        if (correctCount != 1) {
            errors.add("SingleChoice task must have exactly one correct option");
        }

        Set<String> optionTexts = new HashSet<>();
        for (OptionDTO option : options) {
            String optionText = option.getOption();
            if (optionText != null) {
                optionText = optionText.trim();

                if (optionText.equals(statement)) {
                    errors.add("Option cannot be equal to the statement");
                }

                if (!optionTexts.add(optionText)) {
                    errors.add("Options cannot be equal to each other");
                }
            }
        }

        return errors;
    }
}
