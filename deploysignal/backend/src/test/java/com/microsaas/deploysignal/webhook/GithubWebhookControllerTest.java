package com.microsaas.deploysignal.webhook;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GithubWebhookControllerTest {

    @Test
    public void testHandleWebhook() {
        GithubWebhookController controller = new GithubWebhookController();
        Map<String, Object> payload = new HashMap<>();
        ResponseEntity<String> response = controller.handleGithubWebhook("workflow_run", payload);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Received", response.getBody());
    }
}
