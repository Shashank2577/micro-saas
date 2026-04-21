package com.microsaas.jobcraftai;

import com.microsaas.jobcraftai.model.JobPosting;
import com.microsaas.jobcraftai.service.JobCraftService;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
class JobCraftServiceTest {

    @Autowired
    private JobCraftService jobCraftService;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testCreateAndPublish() {
        JobPosting posting = new JobPosting();
        posting.setTitle("Software Engineer");
        posting.setDescription("We are looking for a senior engineer.");

        JobPosting created = jobCraftService.create(posting);
        assertNotNull(created.getId());
        assertEquals("DRAFT", created.getStatus());
        assertEquals(tenantId, created.getTenantId());

        JobPosting optimized = jobCraftService.optimize(created.getId());
        assertEquals("OPTIMIZED", optimized.getStatus());
        assertTrue(optimized.getOptimizedDescription().contains("[AI OPTIMIZED]"));

        JobPosting published = jobCraftService.publish(optimized.getId());
        assertEquals("PUBLISHED", published.getStatus());

        List<JobPosting> all = jobCraftService.getAllForTenant();
        assertFalse(all.isEmpty());
        assertEquals(1, all.size());
        assertEquals("Software Engineer", all.getFirst().getTitle());
    }
}
