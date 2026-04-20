import os

models = [
    ("Employee", "employees", "employees"),
    ("OnboardingWorkflow", "workflows", "onboarding_workflows"),
    ("OnboardingTask", "tasks", "onboarding_tasks"),
    ("TaskAssignment", "assignments", "task_assignments"),
    ("Document", "documents", "documents"),
    ("SystemProvisioningRequest", "provisioning", "system_provisioning_requests"),
    ("BuddyPair", "buddy-pairs", "buddy_pairs"),
    ("OnboardingFeedback", "feedback", "onboarding_feedback")
]

base_dir = "backend/src/test/java/com/microsaas/onboardflow"

os.makedirs(f"{base_dir}/controller", exist_ok=True)
os.makedirs(f"{base_dir}/service", exist_ok=True)

# Generate Controller Tests
for model_name, api_path, table_name in models:
    content = f"""package com.microsaas.onboardflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.onboardflow.dto.{model_name}Request;
import com.microsaas.onboardflow.model.{model_name};
import com.microsaas.onboardflow.service.{model_name}Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {model_name}Controller.class)
public class {model_name}ControllerTest {{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private {model_name}Service service;

    @Test
    public void testGetAll() throws Exception {{
        UUID tenantId = UUID.randomUUID();
        when(service.findAll(tenantId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/onboardflow/{api_path}")
                .header("X-Tenant-ID", tenantId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }}

    @Test
    public void testCreate() throws Exception {{
        UUID tenantId = UUID.randomUUID();
        {model_name}Request request = new {model_name}Request();
        request.setName("Test");

        {model_name} response = new {model_name}();
        response.setName("Test");

        when(service.create(eq(tenantId), any({model_name}Request.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/onboardflow/{api_path}")
                .header("X-Tenant-ID", tenantId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test"));
    }}
}}
"""
    with open(f"{base_dir}/controller/{model_name}ControllerTest.java", "w") as f:
        f.write(content)

# Generate Service Tests
for model_name, api_path, table_name in models:
    content = f"""package com.microsaas.onboardflow.service;

import com.microsaas.onboardflow.dto.{model_name}Request;
import com.microsaas.onboardflow.model.{model_name};
import com.microsaas.onboardflow.repository.{model_name}Repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class {model_name}ServiceTest {{

    @Mock
    private {model_name}Repository repository;

    @InjectMocks
    private {model_name}Service service;

    @Test
    public void testFindAll() {{
        UUID tenantId = UUID.randomUUID();
        when(repository.findByTenantId(tenantId)).thenReturn(Collections.emptyList());

        assertTrue(service.findAll(tenantId).isEmpty());
        verify(repository, times(1)).findByTenantId(tenantId);
    }}

    @Test
    public void testCreate() {{
        UUID tenantId = UUID.randomUUID();
        {model_name}Request request = new {model_name}Request();
        request.setName("Test");

        {model_name} savedEntity = new {model_name}();
        savedEntity.setName("Test");

        when(repository.save(any({model_name}.class))).thenReturn(savedEntity);

        {model_name} result = service.create(tenantId, request);
        assertEquals("Test", result.getName());
    }}

    @Test
    public void testFindById_NotFound() {{
        UUID tenantId = UUID.randomUUID();
        UUID id = UUID.randomUUID();

        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.findById(id, tenantId));
    }}
}}
"""
    with open(f"{base_dir}/service/{model_name}ServiceTest.java", "w") as f:
        f.write(content)
