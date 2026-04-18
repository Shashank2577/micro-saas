package com.microsaas.nonprofitos.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.nonprofitos.ai.AiService;
import com.microsaas.nonprofitos.domain.Donor;
import com.microsaas.nonprofitos.dto.DonorDto;
import com.microsaas.nonprofitos.repository.DonorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DonorService {

    private final DonorRepository donorRepository;
    private final AiService aiService;

    public DonorService(DonorRepository donorRepository, AiService aiService) {
        this.donorRepository = donorRepository;
        this.aiService = aiService;
    }

    public List<Donor> getAllDonors() {
        UUID tenantId = TenantContext.require();
        return donorRepository.findByTenantId(tenantId);
    }

    public Donor createDonor(DonorDto dto) {
        UUID tenantId = TenantContext.require();
        Donor donor = new Donor();
        donor.setTenantId(tenantId);
        donor.setName(dto.getName());
        donor.setEmail(dto.getEmail());
        donor.setTotalGiven(dto.getTotalGiven());
        donor.setEngagementScore(dto.getEngagementScore());
        return donorRepository.save(donor);
    }

    public String getDonorIntelligence(UUID id) {
        UUID tenantId = TenantContext.require();
        Donor donor = donorRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Donor not found"));

        String intelligence = aiService.getDonorIntelligence(donor.getName(), donor.getTotalGiven());
        
        donor.setUpgradePotential(intelligence);
        donorRepository.save(donor);
        
        return intelligence;
    }
}
