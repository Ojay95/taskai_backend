package com.taskai_backend.notification.scheduler;

import com.taskai_backend.task.entity.Task;
import com.taskai_backend.task.repository.TaskRepository;
import com.taskai_backend.notification.service.GeminiAudioService;
import com.taskai_backend.notification.entity.VoicePersona;
import com.taskai_backend.notification.repository.VoicePersonaRepository;
import com.taskai_backend.common.util.AudioFileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReminderJob {

    private final TaskRepository taskRepository;
    private final VoicePersonaRepository personaRepository;
    private final GeminiAudioService geminiService;
    private final AudioFileUtils audioUtils;

    @Scheduled(cron = "0 * * * * *")
    public void checkReminders() {
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        List<Task> dueTasks = taskRepository.findTasksDueAt(now);

        for (Task task : dueTasks) {
            VoicePersona persona = personaRepository.findByUserId(task.getUser().getId())
                    .orElse(VoicePersona.builder().voiceName("Puck").build());

            // 1. Single-call generation of Script + Audio
            byte[] audioData = geminiService.generateReminderAudio(
                    task.getUser().getFullName(),
                    task.getTitle(),
                    task.getDescription(),
                    task.getAiTone(),
                    persona.getVoiceName()
            );

            // 2. Save binary to Storage
            String url = audioUtils.saveAudioFile(audioData, "rem_" + task.getId() + ".wav");

            // 3. Persist URL for the Frontend to play
            task.setVoiceFileUrl(url);
            taskRepository.save(task);

            // 4. Trigger WebSocket or Push Notification
            // pushService.send(task.getUser().getEmail(), url);
        }
    }
}