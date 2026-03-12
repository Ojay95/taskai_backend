package com.taskai_backend.notification.controller;

import com.taskai_backend.identity.entity.User;
import com.taskai_backend.identity.repository.UserRepository;
import com.taskai_backend.notification.dto.request.VoiceSettingsRequest;
import com.taskai_backend.notification.dto.response.VoicePersonaResponse;
import com.taskai_backend.notification.entity.VoicePersona;
import com.taskai_backend.notification.repository.VoicePersonaRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/voice-settings")
@RequiredArgsConstructor
public class VoiceSettingsController {

    private final VoicePersonaRepository personaRepository;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<VoicePersonaResponse> getUserVoiceSettings(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User context not found"));

        VoicePersona persona = personaRepository.findByUserId(user.getId())
                .orElse(VoicePersona.builder().voiceName("Puck").build());

        return ResponseEntity.ok(VoicePersonaResponse.builder()
                .voiceName(persona.getVoiceName())
                .email(user.getEmail())
                .build());
    }

    @PostMapping
    public ResponseEntity<String> updateVoiceSettings(
            @Valid @RequestBody VoiceSettingsRequest request,
            Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User context not found"));

        VoicePersona persona = personaRepository.findByUserId(user.getId())
                .orElse(new VoicePersona());

        persona.setUserId(user.getId());
        persona.setVoiceName(request.getVoiceName());

        personaRepository.save(persona);
        return ResponseEntity.ok("Voice settings updated successfully");
    }
}
