package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.course.Course;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;

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
        Assert.hasText(statement, "Statement cannot be null or empty");
        Assert.notNull(orderPosition, "Order position cannot be null");
        Assert.isTrue(orderPosition > 0, "Order position must be positive");
        Assert.notNull(course, "Course cannot be null");

        this.statement = statement;
        this.orderPosition = orderPosition;
        this.course = course;
    }

    public abstract List<String> validate();

    public boolean canBeAddedToCourse() {
        return course.getStatus() == br.com.alura.AluraFake.course.Status.BUILDING;
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
