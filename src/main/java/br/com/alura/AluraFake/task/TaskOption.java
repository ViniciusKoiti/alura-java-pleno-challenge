package br.com.alura.AluraFake.task;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "Task_Option")
public class TaskOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Length(min = 4, max = 80)
    @Column(nullable = false)
    private String text;

    @NotNull
    @Column(nullable = false)
    private Boolean isCorrect;

    @Positive
    @Column(name = "option_order", nullable = false)
    private Integer optionOrder;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Deprecated
    protected TaskOption() {}

    public TaskOption(String text, Boolean isCorrect, Integer optionOrder, Task task) {
        this.text = text;
        this.isCorrect = isCorrect;
        this.optionOrder = optionOrder;
        this.task = task;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public Integer getOptionOrder() {
        return optionOrder;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}