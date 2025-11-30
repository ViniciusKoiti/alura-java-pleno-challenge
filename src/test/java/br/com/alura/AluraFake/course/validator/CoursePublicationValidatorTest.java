package br.com.alura.AluraFake.course.validator;

import br.com.alura.AluraFake.course.entities.Course;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CoursePublicationValidatorTest {

    @Mock
    private StatusValidator statusValidator;

    @Mock
    private TaskTypesValidator taskTypesValidator;

    @Mock
    private SequenceValidator sequenceValidator;

    @InjectMocks
    private CoursePublicationValidator validator;

    @Test
    void should_collect_errors_from_all_validators() {
        Course course = new Course();
        when(statusValidator.validate(course)).thenReturn(List.of("status error"));
        when(taskTypesValidator.validate(10L)).thenReturn(List.of("types error"));
        when(sequenceValidator.validate(10L)).thenReturn(List.of("sequence error"));

        List<String> errors = validator.validatePublication(course, 10L);

        assertThat(errors).containsExactlyInAnyOrder("status error", "types error", "sequence error");
    }

    @Test
    void should_return_empty_list_when_no_errors() {
        Course course = new Course();
        when(statusValidator.validate(course)).thenReturn(List.of());
        when(taskTypesValidator.validate(10L)).thenReturn(List.of());
        when(sequenceValidator.validate(10L)).thenReturn(List.of());

        List<String> errors = validator.validatePublication(course, 10L);

        assertThat(errors).isEmpty();
    }
}
