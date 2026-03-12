package com.taskai_backend.task.repository;

import com.taskai_backend.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUserEmail(String email);

    List<Task> findTasksDueAt(LocalDateTime now);
}
