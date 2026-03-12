package com.taskai_backend.notification.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "voice_personas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class VoicePersona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long userId;

    // 2026 Gemini High-Def Voices: Puck (Upbeat), Kore (Firm), Aoede (Breezy)
    private String voiceName = "Puck";

    // Used for Gemini speech_config
    private double pitch = 1.0;
    private double speakingRate = 1.0;
}