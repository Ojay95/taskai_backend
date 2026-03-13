package com.taskai_backend.notification.scheduler;



import com.taskai_backend.task.entity.Task;
import com.taskai_backend.task.repository.TaskRepository;
import com.taskai_backend.notification.service.GeminiAudioService;
import com.taskai_backend.common.util.AudioFileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReminderJob {

    private final TaskRepository taskRepository;
    private final GeminiAudioService geminiService;
    private final AudioFileUtils audioUtils;

    @Scheduled(cron = "0 * * * * *") // Runs at the start of every minute
    public void processMinuteReminders() {
        // Define the window: from current minute :00 to :59
        LocalDateTime startOfMinute = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime endOfMinute = startOfMinute.plusSeconds(59);

        log.info("Checking for reminders between {} and {}", startOfMinute, endOfMinute);

        List<Task> dueTasks = taskRepository.findByReminderTimeBetween(startOfMinute, endOfMinute);

        for (Task task : dueTasks) {
            try {
                // 1. Generate Native Audio via Gemini
                byte[] audioData = geminiService.generateReminderAudio(
                        task.getUser().getFullName(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getAiTone(),
                        "Puck" // Default voice, can be pulled from VoicePersona
                );

                // 2. Save and set URL
                String url = audioUtils.saveAudioFile(audioData, "task_" + task.getId() + ".wav");
                task.setVoiceFileUrl(url);

                taskRepository.save(task);
                log.info("AI Voice generated for Task ID: {}", task.getId());

                // TODO: Trigger your WebSocket/Push Notification here

            } catch (Exception e) {
                log.error("Failed to process AI reminder for task {}: {}", task.getId(), e.getMessage());
            }
        }
    }
}