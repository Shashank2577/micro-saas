package com.microsaas.vendormonitor.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.vendormonitor.domain.Vendor;
import com.microsaas.vendormonitor.repository.VendorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VendorServiceTest {

    @Mock
    private VendorRepository vendorRepository;

    @InjectMocks
    private VendorService vendorService;

    private AutoCloseable closeable;
    private final UUID tenantId = UUID.randomUUID();
    private final UUID vendorId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() throws Exception {
        TenantContext.clear();
        closeable.close();
    }

    @Test
    void getAllVendors() {
        Vendor v = new Vendor();
        v.setId(vendorId);
        when(vendorRepository.findByTenantId(tenantId)).thenReturn(List.of(v));

        List<Vendor> result = vendorService.getAllVendors();
        assertEquals(1, result.size());
        assertEquals(vendorId, result.get(0).getId());
    }

    @Test
    void getVendorById_Found() {
        Vendor v = new Vendor();
        v.setId(vendorId);
        when(vendorRepository.findByIdAndTenantId(vendorId, tenantId)).thenReturn(Optional.of(v));

        Optional<Vendor> result = vendorService.getVendorById(vendorId);
        assertTrue(result.isPresent());
        assertEquals(vendorId, result.get().getId());
    }

    @Test
    void createVendor() {
        Vendor v = new Vendor();
        v.setName("Test Vendor");

        when(vendorRepository.save(any(Vendor.class))).thenAnswer(invocation -> {
            Vendor saved = invocation.getArgument(0);
            saved.setId(vendorId);
            return saved;
        });

        Vendor result = vendorService.createVendor(v);

        assertNotNull(result);
        assertEquals(vendorId, result.getId());
        assertEquals(tenantId, result.getTenantId());
        assertEquals("ACTIVE", result.getStatus());
        verify(vendorRepository, times(1)).save(any(Vendor.class));
    }

    @Test
    void updateVendor() {
        Vendor existing = new Vendor();
        existing.setId(vendorId);
        existing.setName("Old Name");
        
        Vendor updates = new Vendor();
        updates.setName("New Name");

        when(vendorRepository.findByIdAndTenantId(vendorId, tenantId)).thenReturn(Optional.of(existing));
        when(vendorRepository.save(any(Vendor.class))).thenReturn(existing);

        Vendor result = vendorService.updateVendor(vendorId, updates);

        assertEquals("New Name", result.getName());
        verify(vendorRepository, times(1)).save(existing);
    }

    @Test
    void deleteVendor() {
        Vendor existing = new Vendor();
        existing.setId(vendorId);

        when(vendorRepository.findByIdAndTenantId(vendorId, tenantId)).thenReturn(Optional.of(existing));

        vendorService.deleteVendor(vendorId);

        verify(vendorRepository, times(1)).delete(existing);
    }
}
