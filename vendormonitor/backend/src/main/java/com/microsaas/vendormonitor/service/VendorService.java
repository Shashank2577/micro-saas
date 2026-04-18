package com.microsaas.vendormonitor.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.vendormonitor.domain.Vendor;
import com.microsaas.vendormonitor.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;

    @Autowired
    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    public List<Vendor> getAllVendors() {
        return vendorRepository.findByTenantId(TenantContext.require());
    }

    public Optional<Vendor> getVendorById(UUID id) {
        return vendorRepository.findByIdAndTenantId(id, TenantContext.require());
    }

    public Vendor createVendor(Vendor vendor) {
        vendor.setTenantId(TenantContext.require());
        if (vendor.getStatus() == null) {
            vendor.setStatus("ACTIVE");
        }
        return vendorRepository.save(vendor);
    }

    public Vendor updateVendor(UUID id, Vendor vendorUpdates) {
        Vendor existingVendor = vendorRepository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found"));
        
        existingVendor.setName(vendorUpdates.getName());
        existingVendor.setCategory(vendorUpdates.getCategory());
        existingVendor.setStatus(vendorUpdates.getStatus());
        existingVendor.setContactEmail(vendorUpdates.getContactEmail());
        existingVendor.setWebsite(vendorUpdates.getWebsite());
        existingVendor.setSlaDescription(vendorUpdates.getSlaDescription());
        
        return vendorRepository.save(existingVendor);
    }

    public void deleteVendor(UUID id) {
        Vendor existingVendor = vendorRepository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found"));
        vendorRepository.delete(existingVendor);
    }
}
