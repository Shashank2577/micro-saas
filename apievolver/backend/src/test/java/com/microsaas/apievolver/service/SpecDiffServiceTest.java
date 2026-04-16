package com.microsaas.apievolver.service;

import com.microsaas.apievolver.model.ApiChange;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SpecDiffServiceTest {

    private final SpecDiffService specDiffService = new SpecDiffService();

    @Test
    public void testDiffDetectsRemovedEndpoint() {
        String oldSpec = """
                openapi: 3.0.0
                info:
                  title: Sample API
                  version: 1.0.0
                paths:
                  /users:
                    get:
                      responses:
                        '200':
                          description: OK
                  /posts:
                    get:
                      responses:
                        '200':
                          description: OK
                """;

        String newSpec = """
                openapi: 3.0.0
                info:
                  title: Sample API
                  version: 2.0.0
                paths:
                  /users:
                    get:
                      responses:
                        '200':
                          description: OK
                """;

        ApiChange change = specDiffService.diff(oldSpec, newSpec, "1.0.0", "2.0.0", 1L);
        
        assertNotNull(change);
        assertTrue(change.getChanges().contains("Removed endpoint: /posts"));
        assertTrue(change.getBreakingChanges().contains("Removed endpoint: /posts"));
    }

    @Test
    public void testDiffDetectsRemovedProperty() {
        String oldSpec = """
                openapi: 3.0.0
                info:
                  title: Sample API
                  version: 1.0.0
                components:
                  schemas:
                    User:
                      type: object
                      properties:
                        id:
                          type: integer
                        name:
                          type: string
                """;

        String newSpec = """
                openapi: 3.0.0
                info:
                  title: Sample API
                  version: 2.0.0
                components:
                  schemas:
                    User:
                      type: object
                      properties:
                        id:
                          type: integer
                """;

        ApiChange change = specDiffService.diff(oldSpec, newSpec, "1.0.0", "2.0.0", 2L);
        
        assertNotNull(change);
        assertTrue(change.getChanges().contains("Removed property name from schema User"));
        assertTrue(change.getBreakingChanges().contains("Removed property name from schema User"));
    }
}
