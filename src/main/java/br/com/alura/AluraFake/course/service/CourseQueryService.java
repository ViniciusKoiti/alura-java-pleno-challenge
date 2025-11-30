package br.com.alura.AluraFake.course.service;

import br.com.alura.AluraFake.course.dto.CourseListItemDTO;

import java.util.List;

public interface CourseQueryService {

    List<CourseListItemDTO> listAllCourses();
}
