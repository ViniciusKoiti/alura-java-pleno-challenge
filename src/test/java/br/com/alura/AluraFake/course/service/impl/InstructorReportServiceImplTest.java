package br.com.alura.AluraFake.course.service.impl;

import br.com.alura.AluraFake.course.dto.CourseReportItemDTO;
import br.com.alura.AluraFake.course.dto.InstructorCoursesReportDTO;
import br.com.alura.AluraFake.course.entities.Course;
import br.com.alura.AluraFake.course.enums.Status;
import br.com.alura.AluraFake.course.CourseRepository;
import br.com.alura.AluraFake.task.TaskRepository;
import br.com.alura.AluraFake.user.enums.Role;
import br.com.alura.AluraFake.user.entities.User;
import br.com.alura.AluraFake.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstructorReportServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private InstructorReportServiceImpl instructorReportService;

    @Test
    void generateReportThrowsWhenUserNotFound() {
        Long instructorId = 999L;
        when(userRepository.findById(instructorId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> instructorReportService.generateInstructorCoursesReport(instructorId));

        assertEquals("User not found with id: 999", exception.getMessage());
        verify(courseRepository, never()).findByInstructorIdOrderByCreatedAtDesc(anyLong());
        verify(taskRepository, never()).countByCourseId(anyLong());
    }

    @Test
    void generateReportThrowsWhenUserIsNotInstructor() {
        Long studentId = 1L;
        User student = new User("Student Name", "student@example.com", Role.STUDENT, "password");
        when(userRepository.findById(studentId)).thenReturn(Optional.of(student));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> instructorReportService.generateInstructorCoursesReport(studentId));

        assertEquals("User is not an instructor", exception.getMessage());
        verify(courseRepository, never()).findByInstructorIdOrderByCreatedAtDesc(anyLong());
        verify(taskRepository, never()).countByCourseId(anyLong());
    }

    @Test
    void generateReportReturnsEmptyListWhenInstructorHasNoCourses() {
        Long instructorId = 1L;
        User instructor = new User("Instructor Name", "instructor@example.com", Role.INSTRUCTOR, "password");
        
        when(userRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(courseRepository.findByInstructorIdOrderByCreatedAtDesc(instructorId)).thenReturn(Collections.emptyList());
        when(courseRepository.countByInstructorIdAndStatus(instructorId, Status.PUBLISHED)).thenReturn(0L);

        InstructorCoursesReportDTO report = instructorReportService.generateInstructorCoursesReport(instructorId);

        assertNotNull(report);
        assertTrue(report.getCourses().isEmpty());
        assertEquals(0L, report.getPublishedCoursesTotal());
        verify(taskRepository, never()).countByCourseId(anyLong());
    }

    @Test
    void generateReportReturnsCorrectDataWhenInstructorHasCourses() {
        Long instructorId = 1L;
        User instructor = new User("Instructor Name", "instructor@example.com", Role.INSTRUCTOR, "password");
        
        Course course1 = new Course("Course 1", "Description 1", instructor);
        Course course2 = new Course("Course 2", "Description 2", instructor);
        course2.publish(); // Published course
        
        List<Course> courses = List.of(course1, course2);

        when(userRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(courseRepository.findByInstructorIdOrderByCreatedAtDesc(instructorId)).thenReturn(courses);
        when(courseRepository.countByInstructorIdAndStatus(instructorId, Status.PUBLISHED)).thenReturn(1L);
        when(taskRepository.countByCourseId(null)).thenReturn(3L, 5L); // Retorna 3L na primeira chamada, 5L na segunda

        InstructorCoursesReportDTO report = instructorReportService.generateInstructorCoursesReport(instructorId);

        assertNotNull(report);
        assertEquals(2, report.getCourses().size());
        assertEquals(1L, report.getPublishedCoursesTotal());

        List<CourseReportItemDTO> courseItems = report.getCourses();
        
        // Verificar se temos os dois cursos esperados
        assertEquals(2, courseItems.size());
        
        // Verificar se as contagens de atividades estão corretas (pode ser em qualquer ordem)
        List<Long> activityCounts = courseItems.stream()
                .map(CourseReportItemDTO::getActivityCount)
                .sorted()
                .toList();
        assertEquals(List.of(3L, 5L), activityCounts);
        
        // Verificar se temos pelo menos um curso publicado
        boolean hasPublishedCourse = courseItems.stream()
                .anyMatch(item -> item.getPublishedAt() != null);
        assertTrue(hasPublishedCourse);

        verify(taskRepository, times(2)).countByCourseId(null);
    }
}