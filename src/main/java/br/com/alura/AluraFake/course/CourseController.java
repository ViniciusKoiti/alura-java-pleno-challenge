package br.com.alura.AluraFake.course;

import br.com.alura.AluraFake.course.dto.CourseListItemDTO;
import br.com.alura.AluraFake.course.dto.NewCourseDTO;
import br.com.alura.AluraFake.course.service.CourseCreationService;
import br.com.alura.AluraFake.course.service.CoursePublicationService;
import br.com.alura.AluraFake.course.service.CourseQueryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CourseController {

    private final CoursePublicationService coursePublicationService;
    private final CourseCreationService courseCreationService;
    private final CourseQueryService courseQueryService;

    @Autowired
    public CourseController(CoursePublicationService coursePublicationService,
                            CourseCreationService courseCreationService,
                            CourseQueryService courseQueryService) {
        this.coursePublicationService = coursePublicationService;
        this.courseCreationService = courseCreationService;
        this.courseQueryService = courseQueryService;
    }

    @Transactional
    @PostMapping("/course/new")
    public ResponseEntity<Void> createCourse(@Valid @RequestBody NewCourseDTO newCourse) {
        courseCreationService.createCourse(newCourse);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/course/all")
    public ResponseEntity<List<CourseListItemDTO>> listCourses() {
        List<CourseListItemDTO> courses = courseQueryService.listAllCourses();
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/course/{id}/publish")
    public ResponseEntity<Void> publishCourse(@PathVariable("id") Long id) {
        coursePublicationService.publishCourse(id);
        return ResponseEntity.ok().build();
    }
}
