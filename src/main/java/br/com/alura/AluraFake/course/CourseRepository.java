package br.com.alura.AluraFake.course;

import br.com.alura.AluraFake.course.entities.Course;
import br.com.alura.AluraFake.course.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long>{

    Optional<Course> findByIdAndStatus(Long id, Status status);
    
    List<Course> findByInstructorIdOrderByCreatedAtDesc(Long instructorId);
    
    Long countByInstructorIdAndStatus(Long instructorId, Status status);
}
