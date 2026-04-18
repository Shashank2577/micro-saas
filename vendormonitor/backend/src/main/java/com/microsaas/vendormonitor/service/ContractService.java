package com.microsaas.vendormonitor.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.vendormonitor.domain.Contract;
import com.microsaas.vendormonitor.domain.Vendor;
import com.microsaas.vendormonitor.repository.ContractRepository;
import com.microsaas.vendormonitor.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final VendorRepository vendorRepository;

    @Autowired
    public ContractService(ContractRepository contractRepository, VendorRepository vendorRepository) {
        this.contractRepository = contractRepository;
        this.vendorRepository = vendorRepository;
    }

    public List<Contract> getContractsForVendor(UUID vendorId) {
        return contractRepository.findByTenantIdAndVendorId(TenantContext.require(), vendorId);
    }

    public Optional<Contract> getContractById(UUID id) {
        return contractRepository.findByIdAndTenantId(id, TenantContext.require());
    }

    public Contract createContract(UUID vendorId, Contract contract) {
        Vendor vendor = vendorRepository.findByIdAndTenantId(vendorId, TenantContext.require())
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found"));
        
        contract.setTenantId(TenantContext.require());
        contract.setVendor(vendor);
        return contractRepository.save(contract);
    }

    public Contract updateContract(UUID id, Contract contractUpdates) {
        Contract existingContract = contractRepository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new IllegalArgumentException("Contract not found"));
        
        existingContract.setStartDate(contractUpdates.getStartDate());
        existingContract.setEndDate(contractUpdates.getEndDate());
        existingContract.setValueAmount(contractUpdates.getValueAmount());
        existingContract.setValueCurrency(contractUpdates.getValueCurrency());
        existingContract.setSlaResponseTimeMinutes(contractUpdates.getSlaResponseTimeMinutes());
        existingContract.setSlaUptimePercentage(contractUpdates.getSlaUptimePercentage());
        existingContract.setAutoRenew(contractUpdates.getAutoRenew());
        
        return contractRepository.save(existingContract);
    }
}
