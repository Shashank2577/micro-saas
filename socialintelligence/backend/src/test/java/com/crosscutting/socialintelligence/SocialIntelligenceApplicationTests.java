package com.crosscutting.socialintelligence;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@TestPropertySource(properties = {
    "cc.tenancy.enabled=false",
    "spring.flyway.enabled=false",
    "spring.jpa.hibernate.ddl-auto=update",
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=sa"
})
class SocialIntelligenceApplicationTests {

    @Test
    void contextLoads() {
    }
}
