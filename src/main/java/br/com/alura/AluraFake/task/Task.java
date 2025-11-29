package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.course.Course;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private final LocalDateTime createdAt = LocalDateTime.now();

    @NotBlank
    @Size(min = 4, max = 255)
    private String statement;

    @Positive
    @Column(name = "order_position")
    private Integer orderPosition;

    @NotNull
    @ManyToOne
    private Course course;

    @Deprecated
    protected Task() {}

    public Task(String statement, Integer orderPosition, Course course) {
        this.statement = statement;
        this.orderPosition = orderPosition;
        this.course = course;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getStatement() {
        return statement;
    }

    public Integer getOrderPosition() {
        return orderPosition;
    }

    public Course getCourse() {
        return course;
    }

    public abstract Type getType();

    public void setOrderPosition(Integer orderPosition) {
        this.orderPosition = orderPosition;
    }
}
