package com.microsaas.runwaymodeler.service;

import com.microsaas.runwaymodeler.model.RunwayModel;
import com.microsaas.runwaymodeler.repository.RunwayModelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RunwayModelServiceTest {

    @Mock
    private RunwayModelRepository repository;

    @InjectMocks
    private RunwayModelService service;

    private UUID tenantId;
    private UUID modelId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        modelId = UUID.randomUUID();
    }

    @Test
    void createModel_CalculatesRunwayDaysCorrectly() {
        when(repository.save(any(RunwayModel.class))).thenAnswer(i -> i.getArguments()[0]);

        BigDecimal cash = new BigDecimal("100000.00");
        BigDecimal burn = new BigDecimal("10000.00");
        
        RunwayModel model = service.createModel("Test Model", burn, cash, tenantId);

        assertNotNull(model.getId());
        assertEquals("Test Model", model.getName());
        assertEquals(burn, model.getCurrentBurn());
        assertEquals(cash, model.getCurrentCash());
        assertEquals(tenantId, model.getTenantId());
        
        // 100,000 / 10,000 = 10 months * 30 days = 300 days
        assertEquals(300, model.getRunwayDays());
        
        verify(repository).save(any(RunwayModel.class));
    }

    @Test
    void updateBurn_RecalculatesRunwayDaysCorrectly() {
        RunwayModel existingModel = new RunwayModel();
        existingModel.setId(modelId);
        existingModel.setTenantId(tenantId);
        existingModel.setCurrentCash(new BigDecimal("100000.00"));
        existingModel.setCurrentBurn(new BigDecimal("10000.00"));
        existingModel.setRunwayDays(300);

        when(repository.findById(modelId)).thenReturn(Optional.of(existingModel));
        when(repository.save(any(RunwayModel.class))).thenAnswer(i -> i.getArguments()[0]);

        BigDecimal newBurn = new BigDecimal("20000.00");
        RunwayModel updatedModel = service.updateBurn(modelId, newBurn, tenantId);

        assertEquals(newBurn, updatedModel.getCurrentBurn());
        // 100,000 / 20,000 = 5 months * 30 days = 150 days
        assertEquals(150, updatedModel.getRunwayDays());
        
        ArgumentCaptor<RunwayModel> captor = ArgumentCaptor.forClass(RunwayModel.class);
        verify(repository).save(captor.capture());
        assertEquals(150, captor.getValue().getRunwayDays());
    }

    @Test
    void createModel_ZeroBurn_ReturnsMaxRunwayDays() {
        when(repository.save(any(RunwayModel.class))).thenAnswer(i -> i.getArguments()[0]);

        BigDecimal cash = new BigDecimal("100000.00");
        BigDecimal burn = BigDecimal.ZERO;
        
        RunwayModel model = service.createModel("Zero Burn", burn, cash, tenantId);

        assertEquals(Integer.MAX_VALUE, model.getRunwayDays());
    }
}
