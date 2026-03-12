package com.taskai_backend.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class AudioFileUtils {

    // Defined in application.properties as: app.storage.location=uploads/audio
    @Value("${app.storage.location:uploads/audio}")
    private String storageLocation;

    /**
     * Saves raw audio bytes to a file and returns the accessible URL path.
     * @param audioData The raw bytes received from Gemini Native Audio.
     * @param originalName A descriptive name (e.g., "reminder_task_12.wav").
     * @return The path/URL that the frontend will use to play the audio.
     */
    public String saveAudioFile(byte[] audioData, String originalName) {
        try {
            // 1. Ensure the directory exists
            Path root = Paths.get(storageLocation);
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }

            // 2. Generate a unique filename using UUID to prevent overwriting
            String fileName = UUID.randomUUID() + "_" + originalName;
            Path targetFile = root.resolve(fileName);

            // 3. Write bytes to the filesystem
            try (FileOutputStream fos = new FileOutputStream(targetFile.toFile())) {
                fos.write(audioData);
            }

            // 4. Return the relative path (exposed via a WebMvc Resource Handler)
            return "/audio/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("CRITICAL: Failed to save AI voice file to disk.", e);
        }
    }
}
