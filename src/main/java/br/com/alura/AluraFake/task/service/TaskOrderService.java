package br.com.alura.AluraFake.task.service;

import br.com.alura.AluraFake.task.Task;
import br.com.alura.AluraFake.task.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TaskOrderService {

    private final TaskRepository taskRepository;

    public TaskOrderService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public void handleOrderConflict(Long courseId, Integer requestedOrder) {
        Optional<Task> existingTask = taskRepository.findByCourseIdAndOrderPosition(courseId, requestedOrder);
        
        if (existingTask.isPresent()) {
            taskRepository.incrementOrderPositionsFromOrder(courseId, requestedOrder);
        }
    }

    public boolean isSequenceContinuous(Long courseId) {
        return taskRepository.isOrderSequenceContinuous(courseId);
    }

    public boolean hasStatementConflict(Long courseId, String statement) {
        return taskRepository.existsByCourseIdAndStatement(courseId, statement);
    }
}