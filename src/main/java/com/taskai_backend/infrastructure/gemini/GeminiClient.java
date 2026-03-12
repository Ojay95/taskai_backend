package com.taskai_backend.infrastructure.gemini;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GeminiClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${app.ai.gemini.api-key}")
    private String apiKey;

    public Map<String, Object> callNativeAudioApi(Map<String, Object> payload) {
        return webClientBuilder.build()
                .post()
                .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-native-audio:generateContent?key=" + apiKey)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Map.class)
                .block(); // Block is acceptable in the infrastructure layer if called via @Async service
    }
}
