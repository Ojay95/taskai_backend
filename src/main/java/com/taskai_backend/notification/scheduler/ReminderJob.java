package com.taskai_backend.notification.scheduler;

import com.taskai_backend.task.entity.Task;
import com.taskai_backend.task.repository.TaskRepository;
import com.taskai_backend.notification.service.GeminiAudioService;
import com.taskai_backend.common.util.AudioFileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReminderJob {

    private final TaskRepository taskRepository;
    private final GeminiAudioService geminiService;
    private final AudioFileUtils audioUtils;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void processMinuteReminders() {
        // Force the search window to UTC
        LocalDateTime nowUtc = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime startOfMinute = nowUtc.truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime endOfMinute = startOfMinute.plusSeconds(59);

        log.info("Checking UTC window: {} to {}", startOfMinute, endOfMinute);

        List<Task> dueTasks = taskRepository.findByReminderTimeBetween(startOfMinute, endOfMinute);
        log.info("Found {} tasks in UTC window.", dueTasks.size());

        for (Task task : dueTasks) {
            try {
                byte[] audioData = geminiService.generateReminderAudio(
                        task.getUser().getFullName(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getAiTone(),
                        "Puck"
                );

                String url = audioUtils.saveAudioFile(audioData, "task_" + task.getId() + ".wav");

                // Populate the missing columns
                task.setVoiceFileUrl(url);
                task.setGeneratedScript("AI " + task.getAiTone() + " reminder for: " + task.getTitle());

                taskRepository.save(task);
                log.info("Successfully updated Task ID: {}", task.getId());

            } catch (Exception e) {
                log.error("Error processing task {}: {}", task.getId(), e.getMessage());
            }
        }
    }
}