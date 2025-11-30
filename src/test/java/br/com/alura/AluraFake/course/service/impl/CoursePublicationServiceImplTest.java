package br.com.alura.AluraFake.course.service.impl;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.course.CourseRepository;
import br.com.alura.AluraFake.course.Status;
import br.com.alura.AluraFake.course.validator.CoursePublicationValidator;
import br.com.alura.AluraFake.user.Role;
import br.com.alura.AluraFake.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoursePublicationServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CoursePublicationValidator coursePublicationValidator;

    @InjectMocks
    private CoursePublicationServiceImpl coursePublicationService;

    @Test
    void publishCoursePublishesWhenValid() {
        User instructor = new User("Instructor", "instructor@example.com", Role.INSTRUCTOR, "password");
        Course course = new Course("Title", "Description", instructor);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(coursePublicationValidator.validatePublication(course, 1L)).thenReturn(Collections.emptyList());
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Course published = coursePublicationService.publishCourse(1L);

        assertEquals(Status.PUBLISHED, published.getStatus());
        assertNotNull(published.getPublishedAt());
        verify(courseRepository).save(course);
    }

    @Test
    void publishCourseThrowsOnValidationErrors() {
        User instructor = new User("Instructor", "instructor@example.com", Role.INSTRUCTOR, "password");
        Course course = new Course("Title", "Description", instructor);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(coursePublicationValidator.validatePublication(course, 1L)).thenReturn(List.of("error"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> coursePublicationService.publishCourse(1L));

        assertEquals("Validation failed: error", exception.getMessage());
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void publishCourseThrowsWhenCourseNotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> coursePublicationService.publishCourse(99L));

        assertEquals("Course not found with id: 99", exception.getMessage());
        verify(coursePublicationValidator, never()).validatePublication(any(), anyLong());
        verify(courseRepository, never()).save(any(Course.class));
    }
}
