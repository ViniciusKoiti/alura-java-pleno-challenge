package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.task.dto.OpenTextTaskDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {

    @PostMapping("/task/new/opentext")
    public ResponseEntity<OpenTextTaskDTO> newOpenTextExercise(@RequestBody OpenTextTaskDTO task) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
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