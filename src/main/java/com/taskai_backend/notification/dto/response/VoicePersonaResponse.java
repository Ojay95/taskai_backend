package com.taskai_backend.notification.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VoicePersonaResponse {
    private String voiceName;
    private String email; // Included to confirm the context on the frontend
}
