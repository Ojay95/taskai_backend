package com.taskai_backend.task.controller;

import com.taskai_backend.task.dto.request.TaskCreateRequest;
import com.taskai_backend.task.dto.response.TaskResponse;
import com.taskai_backend.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskCreateRequest request,
            Principal principal) {
        // principal.getName() provides the email extracted by JwtAuthenticationFilter
        return ResponseEntity.ok(taskService.createTask(request, principal.getName()));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(Principal principal) {
        return ResponseEntity.ok(taskService.getTasks(principal.getName()));
    }
}
