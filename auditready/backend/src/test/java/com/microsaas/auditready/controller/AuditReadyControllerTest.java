package com.microsaas.auditready.controller;

import com.microsaas.auditready.model.ControlFramework;
import com.microsaas.auditready.service.AuditReadyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuditReadyController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class
})
@ActiveProfiles("test")
public class AuditReadyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuditReadyService service;

    @Test
    void getFrameworks_ShouldReturnList() throws Exception {
        ControlFramework f1 = new ControlFramework();
        f1.setId(UUID.randomUUID());
        f1.setName("ISO 27001");
        
        when(service.getFrameworks()).thenReturn(List.of(f1));

        mockMvc.perform(get("/api/frameworks")
                .header("X-Tenant-ID", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("ISO 27001"));
    }
}
