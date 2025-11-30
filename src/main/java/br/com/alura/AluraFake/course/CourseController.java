package br.com.alura.AluraFake.course;

import br.com.alura.AluraFake.course.service.CourseCreationService;
import br.com.alura.AluraFake.course.service.CoursePublicationService;
import br.com.alura.AluraFake.user.*;
import br.com.alura.AluraFake.util.ErrorItemDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CourseController {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CoursePublicationService coursePublicationService;
    private final CourseCreationService courseCreationService;

    @Autowired
    public CourseController(CourseRepository courseRepository,
                            UserRepository userRepository,
                            CoursePublicationService coursePublicationService,
                            CourseCreationService courseCreationService){
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.coursePublicationService = coursePublicationService;
        this.courseCreationService = courseCreationService;
    }

    @Transactional
    @PostMapping("/course/new")
    public ResponseEntity createCourse(@Valid @RequestBody NewCourseDTO newCourse) {

        Optional<User> possibleAuthor = userRepository.findByEmail(newCourse.getEmailInstructor());

        List<String> errors = courseCreationService.validateInstructor(possibleAuthor);
        if(!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("emailInstructor", String.join(", ", errors)));
        }

        courseCreationService.createCourse(newCourse, possibleAuthor.get());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/course/all")
    public ResponseEntity<List<CourseListItemDTO>> createCourse() {
        List<CourseListItemDTO> courses = courseRepository.findAll().stream()
                .map(CourseListItemDTO::new)
                .toList();
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/course/{id}/publish")
    public ResponseEntity createCourse(@PathVariable("id") Long id) {
        coursePublicationService.publishCourse(id);
        return ResponseEntity.ok().build();
    }

}
