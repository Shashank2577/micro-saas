package com.microsaas.billingsync.controller;

import com.microsaas.billingsync.model.Invoice;
import com.microsaas.billingsync.service.BillingService;
import com.microsaas.billingsync.service.LiteLlmClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest(BillingController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class BillingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BillingService billingService;

    @MockBean
    private LiteLlmClient liteLlmClient;

    @Test
    @WithMockUser
    public void testGetAllInvoices() throws Exception {
        Invoice invoice = new Invoice();
        invoice.setId(UUID.randomUUID());
        invoice.setAmountDue(new BigDecimal("100.00"));
        invoice.setStatus("OPEN");
        
        when(billingService.getAllInvoices()).thenReturn(Collections.singletonList(invoice));
        
        mockMvc.perform(get("/api/invoices"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].status").value("OPEN"))
               .andExpect(jsonPath("$[0].amountDue").value(100.0));
    }
}
