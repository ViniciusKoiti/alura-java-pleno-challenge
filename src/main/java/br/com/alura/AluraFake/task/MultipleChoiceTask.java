package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.course.Course;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("MULTIPLE_CHOICE")
public class MultipleChoiceTask extends Task {

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TaskOption> options = new ArrayList<>();

    @Deprecated
    protected MultipleChoiceTask() {}

    public MultipleChoiceTask(String statement, Integer orderPosition, Course course, List<TaskOption> options) {
        super(statement, orderPosition, course);
        if (options != null) {
            this.options = new ArrayList<>(options);
            options.forEach(option -> {
                if (option != null) {
                    option.setTask(this);
                }
            });
        }
    }

    @Override
    public Type getType() {
        return Type.MULTIPLE_CHOICE;
    }

    public List<TaskOption> getOptions() {
        return new ArrayList<>(options);
    }

    public void addOption(TaskOption option) {
        options.add(option);
        option.setTask(this);
    }
}