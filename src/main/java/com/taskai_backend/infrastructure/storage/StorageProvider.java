package com.taskai_backend.infrastructure.storage;


public interface StorageProvider {
    String store(byte[] data, String filename);
}
