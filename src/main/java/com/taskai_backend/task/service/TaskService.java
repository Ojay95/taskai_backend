package com.taskai_backend.task.service;


import com.taskai_backend.identity.entity.User;
import com.taskai_backend.identity.repository.UserRepository;
import com.taskai_backend.task.dto.request.TaskCreateRequest;
import com.taskai_backend.task.dto.response.TaskResponse;
import com.taskai_backend.task.entity.Task;
import com.taskai_backend.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional
    public TaskResponse createTask(TaskCreateRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .reminderTime(request.getReminderTime())
                .aiTone(request.getAiTone())
                .user(user)
                .build();

        Task savedTask = taskRepository.save(task);
        return mapToResponse(savedTask);
    }

    public List<TaskResponse> getTasks(String email) {
        return taskRepository.findAllByUserEmail(email)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .reminderTime(task.getReminderTime())
                .completed(task.isCompleted())
                .aiTone(task.getAiTone())
                .voiceFileUrl(task.getVoiceFileUrl())
                .build();
    }
}
