package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.course.Course;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("OPEN_TEXT")
public class OpenTextTask extends Task {

    @Deprecated
    protected OpenTextTask() {}

    public OpenTextTask(String statement, Integer orderPosition, Course course) {
        super(statement, orderPosition, course);
    }

    @Override
    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        
        if (getStatement() == null || getStatement().trim().isEmpty()) {
            errors.add("Statement cannot be empty");
        }
        
        if (getOrderPosition() == null || getOrderPosition() <= 0) {
            errors.add("Order position must be positive");
        }
        
        if (!canBeAddedToCourse()) {
            errors.add("Course must be in BUILDING status to receive tasks");
        }
        
        return errors;
    }

    @Override
    public Type getType() {
        return Type.OPEN_TEXT;
    }
}