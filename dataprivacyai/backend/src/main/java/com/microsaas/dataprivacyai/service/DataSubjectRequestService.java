package com.microsaas.dataprivacyai.service;

import com.microsaas.dataprivacyai.domain.DataSubjectRequest;
import com.microsaas.dataprivacyai.domain.RequestStatus;
import com.microsaas.dataprivacyai.domain.RequestType;
import com.microsaas.dataprivacyai.repository.DataSubjectRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DataSubjectRequestService {
    private final DataSubjectRequestRepository repository;

    @Transactional
    public DataSubjectRequest submitRequest(RequestType requestType, String subjectEmail, UUID tenantId) {
        DataSubjectRequest request = new DataSubjectRequest();
        request.setRequestType(requestType);
        request.setSubjectEmail(subjectEmail);
        request.setStatus(RequestStatus.PENDING);
        request.setReceivedAt(LocalDateTime.now());
        request.setDueDate(LocalDate.now().plusDays(30));
        request.setTenantId(tenantId);
        return repository.save(request);
    }

    @Transactional
    public void startProcessing(UUID id, UUID tenantId) {
        Optional<DataSubjectRequest> optionalRequest = repository.findById(id);
        if (optionalRequest.isPresent() && optionalRequest.get().getTenantId().equals(tenantId)) {
            DataSubjectRequest request = optionalRequest.get();
            request.setStatus(RequestStatus.IN_PROGRESS);
            repository.save(request);
        } else {
            throw new IllegalArgumentException("Request not found or unauthorized");
        }
    }

    @Transactional
    public void completeRequest(UUID id, UUID tenantId) {
        Optional<DataSubjectRequest> optionalRequest = repository.findById(id);
        if (optionalRequest.isPresent() && optionalRequest.get().getTenantId().equals(tenantId)) {
            DataSubjectRequest request = optionalRequest.get();
            request.setStatus(RequestStatus.COMPLETED);
            request.setCompletedAt(LocalDateTime.now());
            repository.save(request);
        } else {
            throw new IllegalArgumentException("Request not found or unauthorized");
        }
    }

    public List<DataSubjectRequest> listPendingRequests(UUID tenantId) {
        return repository.findByTenantIdAndStatus(tenantId, RequestStatus.PENDING);
    }

    public List<DataSubjectRequest> listOverdueRequests(UUID tenantId) {
        return repository.findByTenantIdAndDueDateBeforeAndStatusNot(tenantId, LocalDate.now(), RequestStatus.COMPLETED);
    }
}
