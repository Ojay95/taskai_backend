package com.taskai_backend.task.entity;

import com.taskai_backend.identity.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime reminderTime;

    private boolean completed = false;

    @Enumerated(EnumType.STRING)
    private TaskTone aiTone;

    // Fields populated by the Notification/AI context later
    private String generatedScript;
    private String voiceFileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public enum TaskTone {
        GENTLE, SERIOUS, PROFESSIONAL
    }
}