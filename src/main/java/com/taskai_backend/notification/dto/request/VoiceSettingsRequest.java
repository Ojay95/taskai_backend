package com.taskai_backend.notification.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoiceSettingsRequest {
    @NotBlank(message = "Voice name is required")
    private String voiceName; // Matches Gemini prebuilt voices: 'Puck', 'Kore', etc.
}