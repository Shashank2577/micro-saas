package com.microsaas.incidentbrain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ai.chat.client.ChatClient;

@SpringBootTest
@ActiveProfiles("test")
class IncidentBrainApplicationTests {

    @MockBean
    private ChatClient chatClient;

    @Test
    void contextLoads() {
    }

}
