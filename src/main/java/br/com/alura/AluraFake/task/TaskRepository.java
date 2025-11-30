package br.com.alura.AluraFake.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByCourseIdAndOrderPosition(Long courseId, Integer orderPosition);

    List<Task> findByCourseIdOrderByOrderPositionAsc(Long courseId);
    boolean existsByCourseIdAndStatement(Long courseId, String statement);

    @Modifying
    @Query("UPDATE Task t SET t.orderPosition = t.orderPosition + 1 " +
            "WHERE t.course.id = :courseId AND t.orderPosition >= :fromOrder")
    void incrementOrderPositionsFromOrder(@Param("courseId") Long courseId,
                                          @Param("fromOrder") Integer fromOrder);

    @Query("SELECT CASE WHEN COUNT(t) = 0 THEN true " +
            "WHEN MAX(t.orderPosition) <> COUNT(t) THEN false " +
            "WHEN MIN(t.orderPosition) <> 1 THEN false " +
            "ELSE true END " +
            "FROM Task t WHERE t.course.id = :courseId")
    boolean isOrderSequenceContinuous(@Param("courseId") Long courseId);
}