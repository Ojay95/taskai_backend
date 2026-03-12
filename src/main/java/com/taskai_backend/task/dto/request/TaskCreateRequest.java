package com.taskai_backend.task.dto.request;


import com.taskai_backend.task.entity.Task.TaskTone;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskCreateRequest {
    @NotBlank private String title;
    private String description;
    @Future private LocalDateTime reminderTime;
    private TaskTone aiTone;
}

