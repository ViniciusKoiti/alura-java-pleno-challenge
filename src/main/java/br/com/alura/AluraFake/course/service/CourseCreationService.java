package br.com.alura.AluraFake.course.service;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.course.CourseRepository;
import br.com.alura.AluraFake.course.NewCourseDTO;
import br.com.alura.AluraFake.course.validator.CourseCreationValidator;
import br.com.alura.AluraFake.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseCreationService {

    private final CourseRepository courseRepository;
    private final CourseCreationValidator courseCreationValidator;

    public CourseCreationService(CourseRepository courseRepository,
                                 CourseCreationValidator courseCreationValidator) {
        this.courseRepository = courseRepository;
        this.courseCreationValidator = courseCreationValidator;
    }

    public List<String> validateInstructor(Optional<User> possibleInstructor) {
        return courseCreationValidator.validateInstructor(possibleInstructor);
    }

    @Transactional
    public void createCourse(NewCourseDTO newCourse, User instructor) {
        Course course = new Course(newCourse.getTitle(), newCourse.getDescription(), instructor);
        courseRepository.save(course);
    }
}
