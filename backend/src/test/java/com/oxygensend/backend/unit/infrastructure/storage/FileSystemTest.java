package com.oxygensend.backend.unit.infrastructure.storage;

import com.oxygensend.backend.infrastructure.storage.FileSystem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FileSystemTest {

    @InjectMocks
    private FileSystem fileSystem;


    @Test
    void testSave() throws IOException {

        // Arrange
        Path tempDir = Files.createTempDirectory("test");
        Path destination = tempDir.resolve("testfile.txt");
        String content = "Test file content";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());

        // Act
        fileSystem.save(inputStream, destination);

        // Assert
        assertTrue(Files.exists(destination));
        assertEquals(content, new String(Files.readAllBytes(destination)));
    }

    @Test
    void testDelete() throws IOException {
        // Arrange
        Path tempDir = Files.createTempDirectory("test");
        Path testFile = tempDir.resolve("testfile.txt");
        Files.createFile(testFile);

        // Act
        fileSystem.delete(testFile);

        // Assert
        assertFalse(Files.exists(testFile));
    }

    @Test
    void testCreateDirectory() throws IOException {
        // Arrange
        Path tempDir = Files.createTempDirectory("test");
        Path newDir = tempDir.resolve("newDirectory");

        // Act
        fileSystem.createDirectory(newDir);

        // Assert
        assertTrue(Files.isDirectory(newDir));
    }

    @Test
    void testGetResource() throws IOException {
        // Arrange
        Path tempDir = Files.createTempDirectory("test");
        Path testFile = tempDir.resolve("testfile.txt");
        Files.createFile(testFile);

        // Act
        Resource resource = fileSystem.getResource(testFile);

        // Assert
        assertNotNull(resource);
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());
    }

}
