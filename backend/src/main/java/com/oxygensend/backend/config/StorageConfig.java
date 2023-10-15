package com.oxygensend.backend.config;

import com.oxygensend.backend.infrastructure.storage.FileStorageService;
import com.oxygensend.backend.application.storage.StorageProperties;
import com.oxygensend.backend.application.storage.StorageService;
import com.oxygensend.backend.infrastructure.storage.FileSystem;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class StorageConfig {

    private final StorageProperties storageProperties;

    @Bean
    public StorageService fileStorage(FileSystem fileSystem) {
        return new FileStorageService(storageProperties.fullShoppingListLocation(), fileSystem);
    }
}
