package com.microsaas.nonprofitos.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.nonprofitos.ai.AiService;
import com.microsaas.nonprofitos.domain.Donor;
import com.microsaas.nonprofitos.dto.DonorDto;
import com.microsaas.nonprofitos.repository.DonorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DonorServiceTest {

    @Mock
    private DonorRepository donorRepository;

    @Mock
    private AiService aiService;

    @InjectMocks
    private DonorService donorService;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testCreateDonor() {
        DonorDto dto = new DonorDto();
        dto.setName("John Doe");
        dto.setTotalGiven(new BigDecimal("1000.00"));
        dto.setEngagementScore(80);

        Donor savedDonor = new Donor();
        savedDonor.setName("John Doe");
        savedDonor.setTotalGiven(new BigDecimal("1000.00"));

        when(donorRepository.save(any(Donor.class))).thenReturn(savedDonor);

        Donor result = donorService.createDonor(dto);

        assertEquals("John Doe", result.getName());
    }

    @Test
    void testGetDonorIntelligence() {
        UUID donorId = UUID.randomUUID();
        Donor donor = new Donor();
        donor.setName("Jane Doe");
        donor.setTotalGiven(new BigDecimal("5000.00"));

        when(donorRepository.findByIdAndTenantId(donorId, tenantId)).thenReturn(Optional.of(donor));
        when(aiService.getDonorIntelligence(donor.getName(), donor.getTotalGiven())).thenReturn("High upgrade potential");
        when(donorRepository.save(any(Donor.class))).thenReturn(donor);

        String result = donorService.getDonorIntelligence(donorId);

        assertEquals("High upgrade potential", result);
        assertEquals("High upgrade potential", donor.getUpgradePotential());
    }
}
