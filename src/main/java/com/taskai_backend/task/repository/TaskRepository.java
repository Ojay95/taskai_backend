package com.taskai_backend.task.repository;

import com.taskai_backend.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Fixed: Matches the 'reminderTime' field in your Task entity
    List<Task> findByReminderTimeBetween(LocalDateTime start, LocalDateTime end);

    List<Task> findAllByUserEmail(String email);
}