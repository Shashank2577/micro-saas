package com.microsaas.featureflagai;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "cc.tenancy.enabled=false"
})
class FeatureFlagAIApplicationTests {

    @org.springframework.boot.test.mock.mockito.MockBean
    private org.springframework.security.oauth2.jwt.JwtDecoder jwtDecoder;


    @Test
    void contextLoads() {
    }

}
