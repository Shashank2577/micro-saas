package com.microsaas.dataprivacyai.service;

import com.microsaas.dataprivacyai.domain.DataSubjectRequest;
import com.microsaas.dataprivacyai.domain.RequestStatus;
import com.microsaas.dataprivacyai.domain.RequestType;
import com.microsaas.dataprivacyai.repository.DataSubjectRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DataSubjectRequestServiceTest {

    private DataSubjectRequestRepository repository;
    private DataSubjectRequestService service;

    @BeforeEach
    void setUp() {
        repository = mock(DataSubjectRequestRepository.class);
        service = new DataSubjectRequestService(repository);
    }

    @Test
    void submitRequest_setsDueDate30DaysOut() {
        UUID tenantId = UUID.randomUUID();
        when(repository.save(any(DataSubjectRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DataSubjectRequest result = service.submitRequest(RequestType.ACCESS, "test@example.com", tenantId);

        assertEquals(LocalDate.now().plusDays(30), result.getDueDate());
        assertEquals(RequestStatus.PENDING, result.getStatus());
        assertEquals("test@example.com", result.getSubjectEmail());
        assertNotNull(result.getReceivedAt());
    }

    @Test
    void completeRequest_changesStatusToCompleted() {
        UUID tenantId = UUID.randomUUID();
        UUID requestId = UUID.randomUUID();
        DataSubjectRequest request = new DataSubjectRequest();
        request.setId(requestId);
        request.setTenantId(tenantId);
        request.setStatus(RequestStatus.IN_PROGRESS);

        when(repository.findById(requestId)).thenReturn(Optional.of(request));

        service.completeRequest(requestId, tenantId);

        ArgumentCaptor<DataSubjectRequest> captor = ArgumentCaptor.forClass(DataSubjectRequest.class);
        verify(repository).save(captor.capture());

        DataSubjectRequest saved = captor.getValue();
        assertEquals(RequestStatus.COMPLETED, saved.getStatus());
        assertNotNull(saved.getCompletedAt());
    }

    @Test
    void listOverdueRequests_returnsOnlyPastDueItems() {
        UUID tenantId = UUID.randomUUID();
        DataSubjectRequest overdue1 = new DataSubjectRequest();
        DataSubjectRequest overdue2 = new DataSubjectRequest();

        when(repository.findByTenantIdAndDueDateBeforeAndStatusNot(eq(tenantId), any(LocalDate.class), eq(RequestStatus.COMPLETED)))
                .thenReturn(List.of(overdue1, overdue2));

        List<DataSubjectRequest> results = service.listOverdueRequests(tenantId);

        assertEquals(2, results.size());
        verify(repository).findByTenantIdAndDueDateBeforeAndStatusNot(eq(tenantId), eq(LocalDate.now()), eq(RequestStatus.COMPLETED));
    }
}
