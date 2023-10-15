package com.oxygensend.backend.application.storage;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@ConfigurationProperties("storage")
public class StorageProperties {

    private String rootLocation = "upload";
    private String shoppingListLocation = "shopping_list";

    public String fullShoppingListLocation() {
        return rootLocation + "/" + shoppingListLocation;
    }
}
