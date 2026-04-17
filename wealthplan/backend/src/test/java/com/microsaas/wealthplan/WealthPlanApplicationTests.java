package com.microsaas.wealthplan;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import com.crosscutting.starter.ai.AiService;

@SpringBootTest
@ActiveProfiles("test")
class WealthPlanApplicationTests {

    @MockBean
    private AiService aiService;

    @Test
    void contextLoads() {
    }

}
