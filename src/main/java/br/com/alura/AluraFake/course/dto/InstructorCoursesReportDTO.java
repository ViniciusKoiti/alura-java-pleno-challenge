package br.com.alura.AluraFake.course.dto;

import java.io.Serializable;
import java.util.List;

public class InstructorCoursesReportDTO implements Serializable {

    private List<CourseReportItemDTO> courses;
    private Long publishedCoursesTotal;

    public InstructorCoursesReportDTO(List<CourseReportItemDTO> courses, Long publishedCoursesTotal) {
        this.courses = courses;
        this.publishedCoursesTotal = publishedCoursesTotal;
    }

    public List<CourseReportItemDTO> getCourses() {
        return courses;
    }

    public Long getPublishedCoursesTotal() {
        return publishedCoursesTotal;
    }
}