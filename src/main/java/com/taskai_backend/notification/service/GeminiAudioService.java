package com.taskai_backend.notification.service;

import com.taskai_backend.task.entity.Task.TaskTone;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GeminiAudioService {

    private final WebClient.Builder webClientBuilder;

    @Value("${app.ai.gemini.api-key}")
    private String apiKey;

    // src/main/java/com/taskai_backend/notification/service/GeminiAudioService.java

    public byte[] generateReminderAudio(String name, String taskTitle, String taskDesc, TaskTone tone, String voiceName) {
        String prompt = String.format(
                "Address %s. Task: %s. Description: %s. Tone: %s. Limit to 20 words.",
                name, taskTitle, taskDesc, tone
        );

        Map<String, Object> request = Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt)))),
                "generationConfig", Map.of(
                        "responseModalities", List.of("AUDIO"),
                        "speechConfig", Map.of(
                                "voiceConfig", Map.of(
                                        "prebuiltVoiceConfig", Map.of("voiceName", voiceName)
                                )
                        )
                )
        );

        Map<String, Object> response = webClientBuilder.build()
                .post()
                // FIX: Use gemini-1.5-flash or gemini-2.0-flash (ensure no '2.5' unless specifically available)
                .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return extractAudioBytes(response);
    }

    private byte[] extractAudioBytes(Map<String, Object> response) {
        try {
            // Path: candidates[0].content.parts[0].inlineData.data
            List<?> candidates = (List<?>) response.get("candidates");
            Map<?, ?> content = (Map<?, ?>) ((Map<?, ?>) candidates.get(0)).get("content");
            List<?> parts = (List<?>) content.get("parts");
            Map<?, ?> inlineData = (Map<?, ?>) ((Map<?, ?>) parts.get(0)).get("inlineData");
            String base64Audio = (String) inlineData.get("data");
            return Base64.getDecoder().decode(base64Audio);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract Gemini Native Audio", e);
        }
    }
}