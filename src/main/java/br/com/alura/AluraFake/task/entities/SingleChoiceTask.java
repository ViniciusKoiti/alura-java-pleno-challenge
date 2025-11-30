package br.com.alura.AluraFake.task.entities;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.task.enums.Type;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("SINGLE_CHOICE")
public class SingleChoiceTask extends Task {

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TaskOption> options = new ArrayList<>();

    @Deprecated
    protected SingleChoiceTask() {}

    public SingleChoiceTask(@NotNull String statement, @NotNull Integer orderPosition, @NotNull Course course, @NotNull List<TaskOption> options) {
        super(statement, orderPosition, course);
        this.options = new ArrayList<>(options);
        options.forEach(option -> option.setTask(this));
    }

    @Override
    public Type getType() {
        return Type.SINGLE_CHOICE;
    }

    public List<TaskOption> getOptions() {
        return new ArrayList<>(options);
    }

    public void addOption(TaskOption option) {
        options.add(option);
        option.setTask(this);
    }
}