package com.microsaas.vendormonitor.controller;

import com.microsaas.vendormonitor.domain.Contract;
import com.microsaas.vendormonitor.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ContractController {

    private final ContractService contractService;

    @Autowired
    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping("/vendors/{vendorId}/contracts")
    public ResponseEntity<List<Contract>> getContractsForVendor(@PathVariable UUID vendorId) {
        return ResponseEntity.ok(contractService.getContractsForVendor(vendorId));
    }

    @PostMapping("/vendors/{vendorId}/contracts")
    public ResponseEntity<Contract> createContract(@PathVariable UUID vendorId, @RequestBody Contract contract) {
        try {
            return ResponseEntity.ok(contractService.createContract(vendorId, contract));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/contracts/{id}")
    public ResponseEntity<Contract> getContractById(@PathVariable UUID id) {
        return contractService.getContractById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/contracts/{id}")
    public ResponseEntity<Contract> updateContract(@PathVariable UUID id, @RequestBody Contract contract) {
        try {
            return ResponseEntity.ok(contractService.updateContract(id, contract));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
