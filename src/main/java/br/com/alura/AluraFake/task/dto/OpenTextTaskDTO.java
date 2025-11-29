package br.com.alura.AluraFake.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;
public class OpenTextTaskDTO {

    @NotNull
    private final Long courseId;

    @NotNull
    @NotBlank
    @Length(min = 4, max = 255)
    private final String statement;

    @NotNull
    @Positive
    private final Integer order;


    public OpenTextTaskDTO(Long courseId, String statement, Integer order) {
        this.courseId = courseId;
        this.statement = statement;
        this.order = order;
    }

    public Long getCourseId() {
        return courseId;
    }

    public String getStatement() {
        return statement;
    }

    public Integer getOrder() {
        return order;
    }
}