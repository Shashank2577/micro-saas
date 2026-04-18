package com.microsaas.vendormonitor.controller;

import com.microsaas.vendormonitor.domain.Contract;
import com.microsaas.vendormonitor.service.ContractService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ContractControllerTest {

    @Mock
    private ContractService contractService;

    @InjectMocks
    private ContractController contractController;

    private AutoCloseable closeable;
    private final UUID contractId = UUID.randomUUID();
    private final UUID vendorId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void getContractsForVendor() {
        when(contractService.getContractsForVendor(vendorId)).thenReturn(List.of(new Contract()));
        ResponseEntity<List<Contract>> response = contractController.getContractsForVendor(vendorId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getContractById_Found() {
        when(contractService.getContractById(contractId)).thenReturn(Optional.of(new Contract()));
        ResponseEntity<Contract> response = contractController.getContractById(contractId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
