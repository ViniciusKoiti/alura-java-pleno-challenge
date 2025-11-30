package br.com.alura.AluraFake.course.service;

import br.com.alura.AluraFake.course.dto.NewCourseDTO;

public interface CourseCreationService {

    void createCourse(NewCourseDTO newCourse);
}
