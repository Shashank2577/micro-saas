package com.microsaas.vendormonitor.controller;

import com.microsaas.vendormonitor.domain.Vendor;
import com.microsaas.vendormonitor.service.VendorService;
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

class VendorControllerTest {

    @Mock
    private VendorService vendorService;

    @InjectMocks
    private VendorController vendorController;

    private AutoCloseable closeable;
    private final UUID vendorId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllVendors() {
        when(vendorService.getAllVendors()).thenReturn(List.of(new Vendor()));
        ResponseEntity<List<Vendor>> response = vendorController.getAllVendors();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getVendorById_Found() {
        when(vendorService.getVendorById(vendorId)).thenReturn(Optional.of(new Vendor()));
        ResponseEntity<Vendor> response = vendorController.getVendorById(vendorId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getVendorById_NotFound() {
        when(vendorService.getVendorById(vendorId)).thenReturn(Optional.empty());
        ResponseEntity<Vendor> response = vendorController.getVendorById(vendorId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createVendor() {
        when(vendorService.createVendor(any(Vendor.class))).thenReturn(new Vendor());
        ResponseEntity<Vendor> response = vendorController.createVendor(new Vendor());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
