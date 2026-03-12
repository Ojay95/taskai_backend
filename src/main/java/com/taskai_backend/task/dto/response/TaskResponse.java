package com.taskai_backend.task.dto.response;

import com.taskai_backend.task.entity.Task.TaskTone;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime reminderTime;
    private boolean completed;
    private TaskTone aiTone;
    private String voiceFileUrl;
}