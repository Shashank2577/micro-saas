package com.microsaas.apimanager.service;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class SchemaServiceTest {

    private final SchemaService schemaService = new SchemaService();

    @Test
    void testParseSchemaValid() {
        String yaml = "openapi: 3.0.0\n" +
                "info:\n" +
                "  title: Sample API\n" +
                "  version: 1.0.0\n" +
                "paths:\n" +
                "  /users:\n" +
                "    get:\n" +
                "      responses:\n" +
                "        '200':\n" +
                "          description: OK";

        Map<String, Object> parsed = schemaService.parseSchema(yaml);
        assertNotNull(parsed);
        assertEquals("3.0.0", parsed.get("openapi"));
    }

    @Test
    void testParseSchemaInvalid() {
        String yaml = "invalid: true";
        assertThrows(IllegalArgumentException.class, () -> schemaService.parseSchema(yaml));
    }
}
