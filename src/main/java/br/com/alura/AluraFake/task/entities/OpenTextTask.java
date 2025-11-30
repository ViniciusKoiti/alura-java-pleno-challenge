package br.com.alura.AluraFake.task.entities;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.task.enums.Type;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


@Entity
@DiscriminatorValue("OPEN_TEXT")
public class OpenTextTask extends Task {

    @Deprecated
    protected OpenTextTask() {}

    public OpenTextTask(String statement, Integer orderPosition, Course course) {
        super(statement, orderPosition, course);
    }


    @Override
    public Type getType() {
        return Type.OPEN_TEXT;
    }
}