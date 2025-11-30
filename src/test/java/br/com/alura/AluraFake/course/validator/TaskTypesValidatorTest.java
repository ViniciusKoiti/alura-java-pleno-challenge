package br.com.alura.AluraFake.course.validator;

import br.com.alura.AluraFake.task.TaskRepository;
import br.com.alura.AluraFake.task.entities.OpenTextTask;
import br.com.alura.AluraFake.task.entities.Task;
import br.com.alura.AluraFake.task.enums.Type;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskTypesValidatorTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskTypesValidator validator;

    @Test
    void should_report_missing_types_when_not_all_present() {
        Task open = new OpenTextTask("Statement", 1, null);
        when(taskRepository.findByCourseIdOrderByOrderPositionAsc(99L)).thenReturn(List.of(open));

        List<String> errors = validator.validate(99L);

        assertThat(errors).contains("Course must have at least one SINGLE_CHOICE task",
                "Course must have at least one MULTIPLE_CHOICE task");
    }

    @Test
    void should_return_empty_when_all_types_present() {
        Task open = new OpenTextTask("Open", 1, null);
        Task single = new SimpleTask(Type.SINGLE_CHOICE);
        Task multiple = new SimpleTask(Type.MULTIPLE_CHOICE);
        when(taskRepository.findByCourseIdOrderByOrderPositionAsc(99L)).thenReturn(List.of(open, single, multiple));

        List<String> errors = validator.validate(99L);

        assertThat(errors).isEmpty();
    }

    // Helper abstract class to instantiate minimal Task for test purposes
    private static class SimpleTask extends br.com.alura.AluraFake.task.entities.Task {
        private final Type type;

        protected SimpleTask(Type type) {
            super("statement", 1, null);
            this.type = type;
        }

        @Override
        public Type getType() {
            return type;
        }
    }
}
