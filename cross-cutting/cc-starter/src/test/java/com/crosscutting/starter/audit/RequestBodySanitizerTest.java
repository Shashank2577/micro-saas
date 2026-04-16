package com.crosscutting.starter.audit;

import com.crosscutting.starter.CcProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RequestBodySanitizerTest {

    private RequestBodySanitizer sanitizer;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        CcProperties properties = new CcProperties();
        properties.getAudit().setSensitiveFields(
                List.of("password", "secret", "token", "apiKey", "authorization")
        );
        objectMapper = new ObjectMapper();
        sanitizer = new RequestBodySanitizer(properties, objectMapper);
    }

    @Test
    void sanitizesPasswordField() {
        String input = "{\"username\":\"john\",\"password\":\"mysecretpass\"}";
        String result = sanitizer.sanitize(input);

        assertTrue(result.contains("\"password\":\"***\""));
        assertTrue(result.contains("\"username\":\"john\""));
        assertFalse(result.contains("mysecretpass"));
    }

    @Test
    void sanitizesTokenField() {
        String input = "{\"token\":\"abc123\",\"data\":\"hello\"}";
        String result = sanitizer.sanitize(input);

        assertTrue(result.contains("\"token\":\"***\""));
        assertTrue(result.contains("\"data\":\"hello\""));
        assertFalse(result.contains("abc123"));
    }

    @Test
    void sanitizesCaseInsensitive() {
        String input = "{\"Password\":\"secret123\",\"TOKEN\":\"tok456\"}";
        String result = sanitizer.sanitize(input);

        assertTrue(result.contains("\"***\""));
        assertFalse(result.contains("secret123"));
        assertFalse(result.contains("tok456"));
    }

    @Test
    void sanitizesNestedFields() {
        String input = "{\"user\":{\"name\":\"john\",\"password\":\"pass123\"},\"token\":\"tok\"}";
        String result = sanitizer.sanitize(input);

        assertFalse(result.contains("pass123"));
        assertFalse(result.contains("\"token\":\"tok\""));
        assertTrue(result.contains("\"token\":\"***\""));
        assertTrue(result.contains("\"name\":\"john\""));
    }

    @Test
    void sanitizesFieldsInArrays() {
        String input = "[{\"password\":\"p1\"},{\"password\":\"p2\"}]";
        String result = sanitizer.sanitize(input);

        assertFalse(result.contains("p1"));
        assertFalse(result.contains("p2"));
    }

    @Test
    void handlesNullInput() {
        assertNull(sanitizer.sanitize(null));
    }

    @Test
    void handlesBlankInput() {
        assertNull(sanitizer.sanitize("  "));
    }

    @Test
    void handlesUnparseableInput() {
        String result = sanitizer.sanitize("not-json{{{");
        assertEquals("{\"_raw\": \"[unparseable]\"}", result);
    }

    @Test
    void preservesNonSensitiveFields() {
        String input = "{\"email\":\"test@example.com\",\"name\":\"John\"}";
        String result = sanitizer.sanitize(input);

        assertTrue(result.contains("test@example.com"));
        assertTrue(result.contains("John"));
    }

    @Test
    void sanitizesApiKeyField() {
        String input = "{\"apiKey\":\"sk-live-123456\",\"endpoint\":\"https://api.example.com\"}";
        String result = sanitizer.sanitize(input);

        assertTrue(result.contains("\"apiKey\":\"***\""));
        assertFalse(result.contains("sk-live-123456"));
        assertTrue(result.contains("https://api.example.com"));
    }
}
