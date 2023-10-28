package com.oxygensend.backend.application.storage;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {

    private String rootLocation;
    private String shoppingListLocation;

    public String fullShoppingListLocation() {
        return rootLocation + "/" + shoppingListLocation;
    }

    public String getRootLocation() {
        return rootLocation;
    }

    public void setRootLocation(String rootLocation) {
        this.rootLocation = rootLocation;
    }

    public String getShoppingListLocation() {
        return shoppingListLocation;
    }

    public void setShoppingListLocation(String shoppingListLocation) {
        this.shoppingListLocation = shoppingListLocation;
    }
}