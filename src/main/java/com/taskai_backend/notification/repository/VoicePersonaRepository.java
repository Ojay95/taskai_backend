package com.taskai_backend.notification.repository;

import com.taskai_backend.notification.entity.VoicePersona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoicePersonaRepository extends JpaRepository<VoicePersona, Long> {
    Optional<VoicePersona> findByUserId(Long userId);
}
