package br.com.alura.AluraFake.task.service;

import br.com.alura.AluraFake.task.entities.Task;
import br.com.alura.AluraFake.task.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    public boolean isValidOrderPosition(Long courseId, Integer requestedOrder) {
        if (requestedOrder == null || requestedOrder <= 0) {
            return false;
        }

        List<Task> existingTasks = taskRepository.findByCourseIdOrderByOrderPositionAsc(courseId);
        
        if (existingTasks.isEmpty()) {
            return requestedOrder == 1;
        }

        int maxOrder = existingTasks.get(existingTasks.size() - 1).getOrderPosition();
        
        return requestedOrder <= maxOrder + 1;
    }
}