package br.com.alura.AluraFake.course.service;

import br.com.alura.AluraFake.course.CourseListItemDTO;
import br.com.alura.AluraFake.course.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseQueryService {

    private final CourseRepository courseRepository;

    public CourseQueryService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<CourseListItemDTO> listAllCourses() {
        return courseRepository.findAll().stream()
                .map(CourseListItemDTO::new)
                .toList();
    }
}
