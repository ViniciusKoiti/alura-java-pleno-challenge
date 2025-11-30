package br.com.alura.AluraFake.course;

import br.com.alura.AluraFake.course.service.CoursePublicationService;
import br.com.alura.AluraFake.user.*;
import br.com.alura.AluraFake.util.ErrorItemDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
public class CourseController {

    private final CoursePublicationService coursePublicationService;
    private final br.com.alura.AluraFake.course.service.CourseCreationService courseCreationService;

    @Autowired
    public CourseController(CoursePublicationService coursePublicationService,
                            br.com.alura.AluraFake.course.service.CourseCreationService courseCreationService){
        this.coursePublicationService = coursePublicationService;
        this.courseCreationService = courseCreationService;
    }

    @Transactional
    @PostMapping("/course/new")
    public ResponseEntity createCourse(@Valid @RequestBody NewCourseDTO newCourse) {
        courseCreationService.createCourse(newCourse);
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
