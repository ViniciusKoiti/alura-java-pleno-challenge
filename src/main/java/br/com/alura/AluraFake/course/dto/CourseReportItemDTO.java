package br.com.alura.AluraFake.course.dto;

import br.com.alura.AluraFake.course.entities.Course;
import br.com.alura.AluraFake.course.enums.Status;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CourseReportItemDTO implements Serializable {

    private Long id;
    private String title;
    private Status status;
    private LocalDateTime publishedAt;
    private Long activityCount;

    public CourseReportItemDTO(Course course, Long activityCount) {
        this.id = course.getId();
        this.title = course.getTitle();
        this.status = course.getStatus();
        this.publishedAt = course.getPublishedAt();
        this.activityCount = activityCount;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public Long getActivityCount() {
        return activityCount;
    }
}