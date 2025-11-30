package br.com.alura.AluraFake.course.service.impl;

import br.com.alura.AluraFake.course.dto.CourseListItemDTO;
import br.com.alura.AluraFake.course.CourseRepository;
import br.com.alura.AluraFake.course.service.CourseQueryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseQueryServiceImpl implements CourseQueryService {

    private final CourseRepository courseRepository;

    public CourseQueryServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<CourseListItemDTO> listAllCourses() {
        return courseRepository.findAll().stream()
                .map(CourseListItemDTO::new)
                .toList();
    }
}
