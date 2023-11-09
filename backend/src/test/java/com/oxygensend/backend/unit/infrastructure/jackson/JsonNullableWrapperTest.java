package com.oxygensend.backend.unit.infrastructure.jackson;


import com.oxygensend.backend.infrastructure.jackson.JsonNullableWrapper;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.junit.jupiter.api.Assertions.*;

public class JsonNullableWrapperTest {

    @Test
    public void testWrap() {
        String value = "test";
        JsonNullable<String> jsonNullable = JsonNullableWrapper.wrap(value);
        assertNotNull(jsonNullable);
        assertTrue(jsonNullable.isPresent());
        assertEquals(value, jsonNullable.get());
    }

    @Test
    public void testUnwrap() {
        String value = "test";
        JsonNullable<String> jsonNullable = JsonNullable.of(value);
        String unwrappedValue = JsonNullableWrapper.unwrap(jsonNullable);
        assertEquals(value, unwrappedValue);
    }

    @Test
    public void testUnwrapWithNullJsonNullable() {
        String unwrappedValue = JsonNullableWrapper.unwrap(null);
        assertNull(unwrappedValue);
    }

    @Test
    public void testIsPresentWithPresentJsonNullable() {
        JsonNullable<String> jsonNullable = JsonNullable.of("test");
        assertTrue(JsonNullableWrapper.isPresent(jsonNullable));
    }

    @Test
    public void testIsPresentWithNullJsonNullable() {
        assertFalse(JsonNullableWrapper.isPresent(null));
    }

}
