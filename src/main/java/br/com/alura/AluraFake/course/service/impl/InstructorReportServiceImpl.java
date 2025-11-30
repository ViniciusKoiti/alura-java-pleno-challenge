package br.com.alura.AluraFake.course.service.impl;

import br.com.alura.AluraFake.course.dto.CourseReportItemDTO;
import br.com.alura.AluraFake.course.dto.InstructorCoursesReportDTO;
import br.com.alura.AluraFake.course.entities.Course;
import br.com.alura.AluraFake.course.enums.Status;
import br.com.alura.AluraFake.course.CourseRepository;
import br.com.alura.AluraFake.course.service.InstructorReportService;
import br.com.alura.AluraFake.task.TaskRepository;
import br.com.alura.AluraFake.user.entities.User;
import br.com.alura.AluraFake.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InstructorReportServiceImpl implements InstructorReportService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final TaskRepository taskRepository;

    public InstructorReportServiceImpl(UserRepository userRepository,
                                       CourseRepository courseRepository,
                                       TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public InstructorCoursesReportDTO generateInstructorCoursesReport(Long instructorId) {
        User user = userRepository.findById(instructorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + instructorId));

        if (!user.isInstructor()) {
            throw new IllegalArgumentException("User is not an instructor");
        }

        List<Course> courses = courseRepository.findByInstructorIdOrderByCreatedAtDesc(instructorId);
        Long publishedCoursesTotal = courseRepository.countByInstructorIdAndStatus(instructorId, Status.PUBLISHED);

        List<CourseReportItemDTO> courseReportItems = new ArrayList<>();
        for (Course course : courses) {
            Long activityCount = taskRepository.countByCourseId(course.getId());
            CourseReportItemDTO reportItem = new CourseReportItemDTO(course, activityCount);
            courseReportItems.add(reportItem);
        }

        return new InstructorCoursesReportDTO(courseReportItems, publishedCoursesTotal);
    }
}