package br.com.alura.AluraFake.course.service.impl;

import br.com.alura.AluraFake.course.entities.Course;
import br.com.alura.AluraFake.course.CourseRepository;
import br.com.alura.AluraFake.course.service.CoursePublicationService;
import br.com.alura.AluraFake.course.validator.CoursePublicationValidator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoursePublicationServiceImpl implements CoursePublicationService {

    private final CourseRepository courseRepository;
    private final CoursePublicationValidator coursePublicationValidator;

    public CoursePublicationServiceImpl(CourseRepository courseRepository,
                                        CoursePublicationValidator coursePublicationValidator) {
        this.courseRepository = courseRepository;
        this.coursePublicationValidator = coursePublicationValidator;
    }

    @Override
    @Transactional
    public Course publishCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + courseId));

        List<String> errors = coursePublicationValidator.validatePublication(course, courseId);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(formatValidationErrors(errors));
        }

        course.publish();
        return courseRepository.save(course);
    }

    private String formatValidationErrors(List<String> errors) {
        return "Validation failed: " + String.join(", ", errors);
    }
}
