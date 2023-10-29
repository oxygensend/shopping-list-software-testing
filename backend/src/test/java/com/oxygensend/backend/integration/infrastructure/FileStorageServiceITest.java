package com.oxygensend.backend.integration.infrastructure;

import com.oxygensend.backend.application.storage.StorageProperties;
import com.oxygensend.backend.application.storage.StorageService;
import com.oxygensend.backend.domain.auth.exception.StorageException;
import com.oxygensend.backend.domain.auth.exception.StorageFileNotFoundException;
import com.oxygensend.backend.infrastructure.storage.FileStorageService;
import com.oxygensend.backend.infrastructure.storage.FileSystem;
import com.oxygensend.backend.integration.BaseITest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileStorageServiceITest extends BaseITest {


    private StorageService fileStorageService;

    @Autowired
    private StorageProperties storageProperties;

    @Autowired
    private FileSystem fileSystem;

    @BeforeEach
    public void setUp() {
        fileStorageService = new FileStorageService(storageProperties.rootLocation(), fileSystem);
    }


    @Test
    void testStore() throws IOException {
        // Arrange
        MockMultipartFile attachmentImage = new MockMultipartFile("attachmentImage", "new_test.jpg", MediaType.IMAGE_JPEG_VALUE, "image data".getBytes());

        // Act
        String storedFileName = fileStorageService.store(attachmentImage);
        var filePath = storageProperties.getRootLocation() + "/" + storedFileName;

        // Assert
        assertNotNull(storedFileName);
        assertTrue(Files.exists(Paths.get(filePath)));

        FileUtils.deleteDirectory(Paths.get(storageProperties.getRootLocation()).toFile());
    }

    @Test
    void test_StoreEmptyFile_ThrowException() {
        // Arrange
        MockMultipartFile attachmentImage = new MockMultipartFile("attachmentImage", "new_test.jpg", MediaType.IMAGE_JPEG_VALUE, "".getBytes());

        // Act && Assert
        var exception = assertThrows(StorageException.class, () -> {
            fileStorageService.store(attachmentImage);
        });

        assertEquals("Failed to store empty file.", exception.getMessage());
    }

    @Test
    void testLoad() throws IOException {
        // Arrange
        String testFileName = "testfile.txt";
        Files.createDirectories(Paths.get(storageProperties.rootLocation()));
        File file = new File(storageProperties.rootLocation() + '/' + testFileName);
        file.createNewFile();

        // Act
        Resource resource = fileStorageService.load(testFileName);

        // Assert
        assertNotNull(resource);
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());

        FileUtils.deleteDirectory(Paths.get(storageProperties.getRootLocation()).toFile());
    }

    @Test
    void test_LoadNonExistentFile_ThrowException()  {
        // Arrange
        String nonExistentFileName = "nonexistent.txt";

        // Act && assert
        StorageFileNotFoundException exception = assertThrows(StorageFileNotFoundException.class, () -> {
            fileStorageService.load(nonExistentFileName);
        });

        assertEquals("Could not read file " + nonExistentFileName, exception.getMessage());
    }


    @Test
    void test_LoadMalformedUrl_ThrowException()  {
        // Arrange
        String nonExistentFileName = "nonexistenttxt";

        // Act && assert
        StorageFileNotFoundException exception = assertThrows(StorageFileNotFoundException.class, () -> {
            fileStorageService.load(nonExistentFileName);
        });

        assertEquals("Could not read file " + nonExistentFileName, exception.getMessage());
    }

    @Test
    void testDelete() throws IOException {
        // Arrange
        String testFileName = "testfile.txt";
        Files.createDirectories(Paths.get(storageProperties.rootLocation()));
        File file = new File(storageProperties.rootLocation() + '/' + testFileName);
        file.createNewFile();

        // Act
        fileStorageService.delete(testFileName);

        // Assert
        assertFalse(Files.exists(Paths.get(storageProperties.rootLocation() + '/' + testFileName)));

        FileUtils.deleteDirectory(Paths.get(storageProperties.getRootLocation()).toFile());
    }


}
