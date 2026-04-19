package com.micro.interviewos.service;

import com.micro.interviewos.domain.InterviewPlan;
import com.micro.interviewos.dto.InterviewPlanDTO;
import com.micro.interviewos.repository.InterviewPlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterviewPlanServiceTest {

    @Mock
    private InterviewPlanRepository repository;

    @InjectMocks
    private InterviewPlanService service;

    private UUID id;
    private UUID tenantId;
    private InterviewPlan entity;
    private InterviewPlanDTO dto;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        tenantId = UUID.randomUUID();

        entity = new InterviewPlan();
        entity.setId(id);
        entity.setTenantId(tenantId);
        entity.setName("Test Name");
        entity.setStatus("ACTIVE");

        dto = new InterviewPlanDTO();
        dto.setId(id);
        dto.setTenantId(tenantId);
        dto.setName("Test Name");
        dto.setStatus("ACTIVE");
    }

    @Test
    void findAll_ShouldReturnList() {
        when(repository.findByTenantId(tenantId)).thenReturn(List.of(entity));

        List<InterviewPlanDTO> result = service.findAll(tenantId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Name", result.get(0).getName());
    }

    @Test
    void findById_ShouldReturnDto() {
        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(entity));

        Optional<InterviewPlanDTO> result = service.findById(id, tenantId);

        assertTrue(result.isPresent());
        assertEquals("Test Name", result.get().getName());
    }

    @Test
    void create_ShouldReturnDto() {
        when(repository.save(any(InterviewPlan.class))).thenReturn(entity);

        InterviewPlanDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals("Test Name", result.getName());
    }

    @Test
    void update_ShouldReturnDto() {
        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(entity));
        when(repository.save(any(InterviewPlan.class))).thenReturn(entity);

        InterviewPlanDTO result = service.update(id, tenantId, dto);

        assertNotNull(result);
        assertEquals("Test Name", result.getName());
    }
}
