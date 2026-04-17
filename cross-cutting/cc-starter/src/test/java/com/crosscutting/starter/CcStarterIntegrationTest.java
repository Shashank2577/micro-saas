package com.crosscutting.starter;

import com.crosscutting.starter.audit.SystemAuditLog;
import com.crosscutting.starter.audit.SystemAuditLogRepository;
import com.crosscutting.starter.tenancy.Tenant;
import com.crosscutting.starter.tenancy.TenantService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test that boots the full Spring context against real PostgreSQL + Redis containers.
 * Run with: mvn test -pl cc-starter -Pintegration
 */
@Tag("integration")
@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("integration")
@Testcontainers
class CcStarterIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("pgvector/pgvector:pg16").asCompatibleSubstituteFor("postgres"))
            .withDatabaseName("crosscutting")
            .withUsername("cc")
            .withPassword("cc_test")
            .withInitScript("init-test.sql");

    @SuppressWarnings("resource")
    @Container
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private SystemAuditLogRepository systemAuditLogRepository;

    @Autowired
    private DataSource dataSource;

    @Test
    void contextLoads() {
        assertThat(applicationContext).isNotNull();
    }

    @Test
    void flywayMigrationsRan() throws Exception {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            // Verify cc schema exists by querying the tenants table
            ResultSet rs = stmt.executeQuery(
                    "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'cc' AND table_name = 'tenants')");
            rs.next();
            assertThat(rs.getBoolean(1)).isTrue();

            // Verify audit schema exists
            rs = stmt.executeQuery(
                    "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'audit' AND table_name = 'system_log')");
            rs.next();
            assertThat(rs.getBoolean(1)).isTrue();
        }
    }

    @Test
    void createAndRetrieveTenant() {
        Tenant created = tenantService.createTenant("Integration Corp", "integration-corp");

        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("Integration Corp");
        assertThat(created.getSlug()).isEqualTo("integration-corp");
        assertThat(created.getStatus()).isEqualTo("active");

        // Retrieve by ID
        Tenant found = tenantService.getTenant(created.getId());
        assertThat(found.getName()).isEqualTo("Integration Corp");

        // Retrieve by slug
        Tenant bySlug = tenantService.getTenantBySlug("integration-corp");
        assertThat(bySlug.getId()).isEqualTo(created.getId());
    }

    @Test
    void auditLogRepositoryIsAccessible() {
        // Verify the audit log repository is wired and can query (table exists via Flyway)
        long count = systemAuditLogRepository.count();
        assertThat(count).isGreaterThanOrEqualTo(0);
    }

    @Test
    void canWriteAndReadAuditLog() {
        SystemAuditLog log = new SystemAuditLog();
        log.setEventType("API");
        log.setAction("INTEGRATION_TEST");
        log.setRequestMethod("GET");
        log.setRequestPath("/test");
        log.setResponseStatus(200);
        systemAuditLogRepository.save(log);

        assertThat(log.getId()).isNotNull();
        assertThat(log.getCreatedAt()).isNotNull();

        var found = systemAuditLogRepository.findById(log.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getAction()).isEqualTo("INTEGRATION_TEST");
    }
}
