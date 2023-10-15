package com.oxygensend.backend.infrastructure.storage;

import com.oxygensend.backend.application.storage.StorageService;
import com.oxygensend.backend.domain.auth.exception.StorageException;
import com.oxygensend.backend.domain.auth.exception.StorageFileNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

public class FileStorageService implements StorageService {
    private final Path uploadDir;
    private final FileSystem fileSystem;


    public FileStorageService(String uploadDir, FileSystem fileSystem) {
        validatePath(uploadDir);
        this.uploadDir = Paths.get(uploadDir);
        this.fileSystem = fileSystem;
        init();
    }

    @Override
    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file.");
        }

        var fileName = getRandomFileName() + '.' + getFileExtension(file.getOriginalFilename());
        Path destinationFile = this.uploadDir.resolve(Paths.get(fileName))
                                             .normalize()
                                             .toAbsolutePath();
        if (!destinationFile.getParent().equals(this.uploadDir.toAbsolutePath())) {
            throw new StorageException("Cannot store file outside upload directory");
        }
        try (InputStream inputStream = file.getInputStream()) {
            fileSystem.save(inputStream, destinationFile);
        } catch (IOException e) {
            throw new StorageException("Failed to store file: " + e.getMessage());
        }
        return destinationFile.getFileName().toString();
    }


    @Override
    public Resource load(String filename) {
        try {
            var file = loadFile(filename);
            var resource = fileSystem.getResource(file);
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file " + filename);
        }
    }

    @Override
    public void delete(String filename) {
        try {
            fileSystem.delete(uploadDir.resolve(filename));
        } catch (IOException e) {
            throw new StorageException("Cannot delete file " + filename);
        }
    }

    private String getRandomFileName() {
        return UUID.randomUUID().toString();
    }

    private String getFileExtension(String filename) {
        return Optional.ofNullable(filename)
                       .filter(f -> f.contains("."))
                       .map(f -> f.substring(filename.lastIndexOf(".") + 1))
                       .orElseThrow(() -> new StorageException("Cannot read extension for file " + filename));
    }

    private Path loadFile(String filename) {
        return uploadDir.resolve(filename);
    }

    private void validatePath(String uploadDir) {
        if (!StringUtils.hasLength(uploadDir)) {
            throw new StorageException("File upload location cannot be empty");
        }
    }

    private void init() {
        try {
            fileSystem.createDirectory(uploadDir);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage: " + e.getMessage());
        }
    }
}
