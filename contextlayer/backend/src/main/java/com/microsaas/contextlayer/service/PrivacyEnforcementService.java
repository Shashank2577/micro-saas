package com.microsaas.contextlayer.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.contextlayer.domain.ConsentRecord;
import com.microsaas.contextlayer.repository.ConsentRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrivacyEnforcementService {
    private final ConsentRecordRepository consentRecordRepository;

    public ConsentRecord recordConsent(String customerId, String type, Boolean granted, String app) {
        ConsentRecord record = consentRecordRepository.findByCustomerIdAndTenantIdAndConsentType(customerId, TenantContext.require(), type)
            .orElseGet(() -> {
                ConsentRecord newRecord = new ConsentRecord();
                newRecord.setCustomerId(customerId);
                newRecord.setTenantId(TenantContext.require());
                newRecord.setConsentType(type);
                return newRecord;
            });

        record.setGranted(granted);
        record.setConsentedAt(Instant.now());
        record.setConsentedByApp(app);
        return consentRecordRepository.save(record);
    }

    public boolean hasConsent(String customerId, String type) {
        Optional<ConsentRecord> record = consentRecordRepository.findByCustomerIdAndTenantIdAndConsentType(customerId, TenantContext.require(), type);
        return record.map(ConsentRecord::getGranted).orElse(false);
    }

    public Optional<ConsentRecord> getConsent(String customerId, String type) {
        return consentRecordRepository.findByCustomerIdAndTenantIdAndConsentType(customerId, TenantContext.require(), type);
    }

    public void revokeConsent(String customerId) {
        List<ConsentRecord> records = consentRecordRepository.findByCustomerIdAndTenantId(customerId, TenantContext.require());
        for (ConsentRecord record : records) {
            record.setGranted(false);
            consentRecordRepository.save(record);
        }
    }

    public List<ConsentRecord> getConsentHistory(String customerId) {
        return consentRecordRepository.findByCustomerIdAndTenantId(customerId, TenantContext.require());
    }
}
