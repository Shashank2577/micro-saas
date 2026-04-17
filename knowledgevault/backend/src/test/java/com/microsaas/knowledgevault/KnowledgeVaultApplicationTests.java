package com.microsaas.knowledgevault;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.search.SearchService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "cc.ai.enabled=false",
    "cc.search.enabled=false",
    "spring.autoconfigure.exclude=com.crosscutting.starter.search.SearchAutoConfiguration"
})
@EnableAutoConfiguration(exclude = {
    DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class,
    FlywayAutoConfiguration.class
})
class KnowledgeVaultApplicationTests {

    @MockBean
    private AiService aiService;

    @MockBean
    private SearchService searchService;
    
    @MockBean
    private JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() {
    }

}
