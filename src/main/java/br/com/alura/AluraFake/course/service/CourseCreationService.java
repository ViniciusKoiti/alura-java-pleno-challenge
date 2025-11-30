package br.com.alura.AluraFake.course.service;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.course.CourseRepository;
import br.com.alura.AluraFake.course.NewCourseDTO;
import br.com.alura.AluraFake.course.validator.CourseCreationValidator;
import br.com.alura.AluraFake.user.User;
import br.com.alura.AluraFake.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseCreationService {

    private final CourseRepository courseRepository;
    private final CourseCreationValidator courseCreationValidator;
    private final UserRepository userRepository;

    public CourseCreationService(CourseRepository courseRepository,
                                 CourseCreationValidator courseCreationValidator,
                                 UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.courseCreationValidator = courseCreationValidator;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createCourse(NewCourseDTO newCourse) {
        Optional<User> possibleInstructor = userRepository.findByEmail(newCourse.getEmailInstructor());
        List<String> errors = courseCreationValidator.validateInstructor(possibleInstructor);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join(", ", errors));
        }

        Course course = new Course(newCourse.getTitle(), newCourse.getDescription(), possibleInstructor.get());
        courseRepository.save(course);
    }
}
