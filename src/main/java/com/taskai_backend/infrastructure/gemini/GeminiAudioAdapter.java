package com.taskai_backend.infrastructure.gemini;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GeminiAudioAdapter {

    private final GeminiClient geminiClient;

    public byte[] getAudioFromPrompt(String prompt, String voiceName) {
        Map<String, Object> payload = Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt)))),
                "generationConfig", Map.of(
                        "responseModalities", List.of("AUDIO"),
                        "speechConfig", Map.of(
                                "voiceConfig", Map.of("prebuiltVoiceConfig", Map.of("voiceName", voiceName))
                        )
                )
        );

        Map<String, Object> response = geminiClient.callNativeAudioApi(payload);
        return parseResponse(response);
    }

    private byte[] parseResponse(Map<String, Object> response) {
        try {
            // Traverse: candidates[0] -> content -> parts[0] -> inlineData -> data
            List<?> candidates = (List<?>) response.get("candidates");
            Map<?, ?> firstCandidate = (Map<?, ?>) candidates.get(0);
            Map<?, ?> content = (Map<?, ?>) firstCandidate.get("content");
            List<?> parts = (List<?>) content.get("parts");
            Map<?, ?> firstPart = (Map<?, ?>) parts.get(0);
            Map<?, ?> inlineData = (Map<?, ?>) firstPart.get("inlineData");
            String base64Data = (String) inlineData.get("data");

            return Base64.getDecoder().decode(base64Data);
        } catch (Exception e) {
            throw new RuntimeException("Infrastructure Error: Malformed Gemini API response", e);
        }
    }
}