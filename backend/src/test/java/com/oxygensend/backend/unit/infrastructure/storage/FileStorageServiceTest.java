package com.oxygensend.backend.unit.infrastructure.storage;

import com.oxygensend.backend.domain.auth.exception.StorageException;
import com.oxygensend.backend.domain.auth.exception.StorageFileNotFoundException;
import com.oxygensend.backend.infrastructure.storage.FileStorageService;
import com.oxygensend.backend.infrastructure.storage.FileSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileStorageServiceTest {

    @Mock
    private MultipartFile mockFile;

    @Mock
    private FileSystem fileSystem;
    private FileStorageService fileStorageService;

    @BeforeEach
    public void setUp() {
        fileStorageService = new FileStorageService("test", fileSystem);
    }

    @Test
    void testStore() throws IOException {
        // Arrange
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getOriginalFilename()).thenReturn("testfile.txt");
        when(mockFile.getInputStream()).thenReturn(InputStream.nullInputStream());

        // Act
        String storedFileName = fileStorageService.store(mockFile);

        // Assert
        assertNotNull(storedFileName);
        assertTrue(storedFileName.endsWith(".txt"));
    }

    @Test
    void test_StoreEmptyFile_ThrowException() {
        // Arrange
        when(mockFile.isEmpty()).thenReturn(true);

        // Act && Assert
        var exception = assertThrows(StorageException.class, () -> {
            fileStorageService.store(mockFile);
        });

        assertEquals("Failed to store empty file.", exception.getMessage());
    }

    @Test
    void testStoreStreamIssue_ThrowException() throws IOException {
        // Arrange
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getOriginalFilename()).thenReturn("testfile.txt");
        when(mockFile.getInputStream()).thenReturn(InputStream.nullInputStream());
        doThrow(new IOException("fail")).when(fileSystem).save(any(InputStream.class), any(Path.class));

        // Act && Assert
        var exception = assertThrows(StorageException.class, () -> {
            fileStorageService.store(mockFile);
        });

        assertEquals("Failed to store file: fail", exception.getMessage());
    }


    @Test
    void testLoad() throws MalformedURLException {
        // Arrange
        String testFileName = "testfile.txt";
        var mockResource = mock(Resource.class);
        when(mockResource.exists()).thenReturn(true);
        when(mockResource.isReadable()).thenReturn(true);
        when(fileSystem.getResource(any(Path.class))).thenReturn(mockResource);

        // Act
        Resource resource = fileStorageService.load(testFileName);

        // Assert
        assertNotNull(resource);
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());
    }

    @Test
    void test_LoadNonExistentFile_ThrowException() throws MalformedURLException {
        // Arrange
        String nonExistentFileName = "nonexistent.txt";
        var mockResource = mock(Resource.class);
        when(mockResource.exists()).thenReturn(false);
        when(fileSystem.getResource(any(Path.class))).thenReturn(mockResource);

        // Act && assert
        StorageFileNotFoundException exception = assertThrows(StorageFileNotFoundException.class, () -> {
            fileStorageService.load(nonExistentFileName);
        });

        assertEquals("Could not read file " + nonExistentFileName, exception.getMessage());
    }


    @Test
    void test_LoadMalformedUrl_ThrowException() throws MalformedURLException {
        // Arrange
        String nonExistentFileName = "nonexistent.txt";
        when(fileSystem.getResource(any(Path.class))).thenThrow(new MalformedURLException());

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

        // Act
        fileStorageService.delete(testFileName);

        // Assert
        verify(fileSystem, times(1)).delete(any(Path.class));
    }

    @Test
    void test_DeleteNonExistentFile_ThrowException() throws IOException {
        // Arrange
        String nonExistentFileName = "nonexistent.txt";
        doThrow(new IOException()).when(fileSystem).delete(any(Path.class));

        // Act && assert
        StorageException exception = assertThrows(StorageException.class, () -> {
            fileStorageService.delete(nonExistentFileName);
        });

        assertEquals("Cannot delete file " + nonExistentFileName, exception.getMessage());
        verify(fileSystem, times(1)).delete(any(Path.class));
    }
}
