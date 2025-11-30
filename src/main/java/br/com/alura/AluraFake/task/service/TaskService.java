package br.com.alura.AluraFake.task.service;

import br.com.alura.AluraFake.task.dto.NewMultipleChoiceTaskDTO;
import br.com.alura.AluraFake.task.dto.NewSingleChoiceTaskDTO;
import br.com.alura.AluraFake.task.dto.OpenTextTaskDTO;

public interface TaskService {

    OpenTextTaskDTO createOpenTextTask(OpenTextTaskDTO openTextTaskDTO);
    
    NewSingleChoiceTaskDTO createSingleChoiceTask(NewSingleChoiceTaskDTO newSingleChoiceTaskDTO);
    
    NewMultipleChoiceTaskDTO createMultipleChoiceTask(NewMultipleChoiceTaskDTO newMultipleChoiceTaskDTO);
}
