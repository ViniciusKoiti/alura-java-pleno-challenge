package br.com.alura.AluraFake.task.validator;

import br.com.alura.AluraFake.course.entities.Course;
import br.com.alura.AluraFake.task.dto.OptionDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskValidator {

    private final OpenTextTaskValidator openTextTaskValidator;
    private final SingleChoiceTaskValidator singleChoiceTaskValidator;
    private final MultipleChoiceTaskValidator multipleChoiceTaskValidator;

    public TaskValidator(OpenTextTaskValidator openTextTaskValidator,
                         SingleChoiceTaskValidator singleChoiceTaskValidator,
                         MultipleChoiceTaskValidator multipleChoiceTaskValidator) {
        this.openTextTaskValidator = openTextTaskValidator;
        this.singleChoiceTaskValidator = singleChoiceTaskValidator;
        this.multipleChoiceTaskValidator = multipleChoiceTaskValidator;
    }

    public List<String> validateOpenTextTask(String statement, Integer orderPosition, Course course) {
        return openTextTaskValidator.validate(statement, orderPosition, course);
    }

    public List<String> validateSingleChoiceTask(String statement, Integer orderPosition, Course course, List<OptionDTO> options) {
        return singleChoiceTaskValidator.validate(statement, orderPosition, course, options);
    }

    public List<String> validateMultipleChoiceTask(String statement, Integer orderPosition, Course course, List<OptionDTO> options) {
        return multipleChoiceTaskValidator.validate(statement, orderPosition, course, options);
    }

    public boolean canTaskBeAddedToCourse(Course course) {
        return openTextTaskValidator.canTaskBeAddedToCourse(course);
    }
}
