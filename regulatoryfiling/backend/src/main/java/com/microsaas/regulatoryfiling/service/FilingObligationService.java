package com.microsaas.regulatoryfiling.service;

import com.microsaas.regulatoryfiling.domain.FilingObligation;
import com.microsaas.regulatoryfiling.domain.FilingStatus;
import com.microsaas.regulatoryfiling.domain.RecurrencePattern;
import com.microsaas.regulatoryfiling.repository.FilingObligationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FilingObligationService {

    private final FilingObligationRepository repository;
    private final AlertService alertService;

    @Transactional
    public FilingObligation addObligation(String name, String jurisdiction, String filingType, LocalDate dueDate, RecurrencePattern recurrencePattern, UUID tenantId) {
        FilingObligation obligation = FilingObligation.builder()
                .id(UUID.randomUUID())
                .name(name)
                .jurisdiction(jurisdiction)
                .filingType(filingType)
                .dueDate(dueDate)
                .recurrencePattern(recurrencePattern)
                .status(dueDate.isBefore(LocalDate.now()) ? FilingStatus.OVERDUE : FilingStatus.UPCOMING)
                .tenantId(tenantId)
                .build();
        
        obligation = repository.save(obligation);
        
        // Generate alerts based on due date
        alertService.generateAlerts(obligation.getId(), tenantId);

        return obligation;
    }

    @Transactional(readOnly = true)
    public List<FilingObligation> listObligations(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public List<FilingObligation> getUpcoming(UUID tenantId, int daysAhead) {
        LocalDate today = LocalDate.now();
        LocalDate targetDate = today.plusDays(daysAhead);
        return repository.findByTenantIdAndDueDateBetween(tenantId, today, targetDate);
    }

    @Transactional(readOnly = true)
    public List<FilingObligation> getOverdue(UUID tenantId) {
        return repository.findByTenantIdAndDueDateBeforeAndStatusNot(tenantId, LocalDate.now(), FilingStatus.SUBMITTED);
    }

    @Transactional
    public FilingObligation submitFiling(UUID id, UUID tenantId) {
        FilingObligation obligation = repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Obligation not found"));
        
        obligation.setStatus(FilingStatus.SUBMITTED);
        return repository.save(obligation);
    }

    @Transactional(readOnly = true)
    public FilingObligation getObligation(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Obligation not found"));
    }
}
