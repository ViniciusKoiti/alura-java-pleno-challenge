package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.task.dto.OpenTextTaskDTO;
import br.com.alura.AluraFake.task.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/task/new/opentext")
    public ResponseEntity<OpenTextTaskDTO> newOpenTextExercise(@Valid @RequestBody OpenTextTaskDTO task) {
        OpenTextTaskDTO created = taskService.createOpenTextTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @PostMapping("/task/new/singlechoice")
    public ResponseEntity<Task> newSingleChoice() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/task/new/multiplechoice")
    public ResponseEntity<Task> newMultipleChoice() {
        return ResponseEntity.ok().build();
    }

}