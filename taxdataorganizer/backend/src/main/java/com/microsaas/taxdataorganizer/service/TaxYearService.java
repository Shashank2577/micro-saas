package com.microsaas.taxdataorganizer.service;

import com.microsaas.taxdataorganizer.model.TaxYear;
import com.microsaas.taxdataorganizer.model.TaxYearStatus;
import com.microsaas.taxdataorganizer.repository.TaxYearRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaxYearService {

    private final TaxYearRepository taxYearRepository;

    @Transactional
    public TaxYear createTaxYear(int year, UUID tenantId) {
        TaxYear taxYear = TaxYear.builder()
                .year(year)
                .status(TaxYearStatus.OPEN)
                .tenantId(tenantId)
                .build();
        return taxYearRepository.save(taxYear);
    }

    @Transactional
    public TaxYear markReadyForAccountant(UUID id, UUID tenantId) {
        TaxYear taxYear = taxYearRepository.findById(id)
                .filter(t -> t.getTenantId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("TaxYear not found"));
        taxYear.setStatus(TaxYearStatus.READY_FOR_ACCOUNTANT);
        return taxYearRepository.save(taxYear);
    }

    @Transactional(readOnly = true)
    public List<TaxYear> listYears(UUID tenantId) {
        return taxYearRepository.findAllByTenantId(tenantId);
    }
}
