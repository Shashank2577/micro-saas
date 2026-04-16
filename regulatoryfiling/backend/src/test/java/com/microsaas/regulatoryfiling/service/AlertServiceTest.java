package com.microsaas.regulatoryfiling.service;

import com.microsaas.regulatoryfiling.domain.FilingAlert;
import com.microsaas.regulatoryfiling.domain.FilingObligation;
import com.microsaas.regulatoryfiling.repository.FilingAlertRepository;
import com.microsaas.regulatoryfiling.repository.FilingObligationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertServiceTest {

    @Mock
    private FilingAlertRepository alertRepository;

    @Mock
    private FilingObligationRepository obligationRepository;

    private AlertService alertService;

    @BeforeEach
    void setUp() {
        alertService = new AlertService(alertRepository, obligationRepository);
    }

    @Test
    void generateAlerts_shouldCreateThreeAlerts() {
        // Arrange
        UUID obligationId = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        LocalDate dueDate = LocalDate.now().plusDays(40);
        
        FilingObligation obligation = new FilingObligation();
        obligation.setId(obligationId);
        obligation.setDueDate(dueDate);
        obligation.setTenantId(tenantId);

        when(obligationRepository.findByIdAndTenantId(obligationId, tenantId)).thenReturn(Optional.of(obligation));

        // Act
        alertService.generateAlerts(obligationId, tenantId);

        // Assert
        ArgumentCaptor<FilingAlert> alertCaptor = ArgumentCaptor.forClass(FilingAlert.class);
        verify(alertRepository, times(3)).save(alertCaptor.capture());

        List<FilingAlert> savedAlerts = alertCaptor.getAllValues();
        assertThat(savedAlerts).hasSize(3);
        
        assertThat(savedAlerts).extracting(FilingAlert::getDaysUntilDue)
                .containsExactlyInAnyOrder(30, 14, 7);
                
        assertThat(savedAlerts).extracting(FilingAlert::getAlertDate)
                .containsExactlyInAnyOrder(
                        dueDate.minusDays(30),
                        dueDate.minusDays(14),
                        dueDate.minusDays(7)
                );
    }

    @Test
    void acknowledgeAlert_shouldMarkAlertAsAcknowledged() {
        // Arrange
        UUID alertId = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        
        FilingAlert alert = new FilingAlert();
        alert.setId(alertId);
        alert.setTenantId(tenantId);
        alert.setAcknowledged(false);

        when(alertRepository.findByIdAndTenantId(alertId, tenantId)).thenReturn(Optional.of(alert));
        when(alertRepository.save(any(FilingAlert.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        FilingAlert acknowledgedAlert = alertService.acknowledgeAlert(alertId, tenantId);

        // Assert
        assertThat(acknowledgedAlert.isAcknowledged()).isTrue();
        verify(alertRepository).save(alert);
    }
}
