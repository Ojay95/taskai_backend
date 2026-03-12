package com.taskai_backend.infrastructure.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.FileOutputStream;
import java.nio.file.*;

@Component
public class LocalStorageAdapter implements StorageProvider {

    @Value("${app.storage.location:uploads/audio}")
    private String uploadPath;

    @Override
    public String store(byte[] data, String filename) {
        try {
            Path root = Paths.get(uploadPath);
            if (!Files.exists(root)) Files.createDirectories(root);

            Path file = root.resolve(filename);
            try (FileOutputStream fos = new FileOutputStream(file.toFile())) {
                fos.write(data);
            }
            return "/audio/" + filename;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save audio to infrastructure storage", e);
        }
    }
}
