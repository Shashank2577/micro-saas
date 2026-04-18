package com.microsaas.observabilityai;

import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ObservabilityServiceTest {

    @Autowired
    private ObservabilitySignalRepository signalRepository;

    @Autowired
    private ObservabilityAlertRepository alertRepository;

    private ObservabilityService service;

    private UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        service = new ObservabilityService(signalRepository, alertRepository);
        TenantContext.set(tenantId);
    }

    @Test
    void createAndGetSignals() {
        ObservabilitySignal signal = new ObservabilitySignal();
        signal.setSignalType("LOG");
        signal.setServiceName("user-service");
        signal.setPayload("{\"message\": \"User logged in\"}");
        signal.setTimestamp(ZonedDateTime.now());

        service.createSignal(signal);

        List<ObservabilitySignal> signals = service.getSignals("LOG");
        assertThat(signals).hasSize(1);
        assertThat(signals.get(0).getTenantId()).isEqualTo(tenantId);
    }
}
