package com.oxygensend.backend.unit.application.storage;

import com.oxygensend.backend.application.storage.StorageProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class StoragePropertiesTest {

    @Test
    void test_GetFullShoppingListLocation() {
        // Arrange
        StorageProperties storageProperties = new StorageProperties();
        storageProperties.rootLocation("upload");
        storageProperties.shoppingListLocation("shopping_list");

        // Act
        String result = storageProperties.fullShoppingListLocation();

        // Assert
        assertEquals("upload/shopping_list", result);
    }

    @Test
    void test_GetShoppingListLocation() {
        // Arrange
        StorageProperties storageProperties = new StorageProperties();
        storageProperties.rootLocation("upload");
        storageProperties.shoppingListLocation("shopping_list");

        // Act
        String result = storageProperties.getShoppingListLocation();

        // Assert
        assertEquals("shopping_list", result);
    }



}
