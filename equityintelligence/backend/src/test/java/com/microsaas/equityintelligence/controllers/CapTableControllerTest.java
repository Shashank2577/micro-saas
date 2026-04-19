package com.microsaas.equityintelligence.controllers;

import com.microsaas.equityintelligence.model.CapTable;
import com.microsaas.equityintelligence.services.CapTableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CapTableControllerTest {

    private CapTableService service;
    private CapTableController controller;

    @BeforeEach
    void setUp() {
        service = mock(CapTableService.class);
        controller = new CapTableController(service);
    }

    @Test
    void getAll_shouldReturnList() {
        CapTable capTable = new CapTable();
        capTable.setName("Test Table");
        when(service.findAll()).thenReturn(List.of(capTable));

        List<CapTable> result = controller.getAll();
        assertEquals(1, result.size());
        assertEquals("Test Table", result.get(0).getName());
    }

    @Test
    void getById_shouldReturnItemWhenFound() {
        UUID id = UUID.randomUUID();
        CapTable capTable = new CapTable();
        when(service.findById(id)).thenReturn(Optional.of(capTable));

        ResponseEntity<CapTable> response = controller.getById(id);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
    }

    @Test
    void getById_shouldReturn404WhenNotFound() {
        UUID id = UUID.randomUUID();
        when(service.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<CapTable> response = controller.getById(id);
        assertTrue(response.getStatusCode().is4xxClientError());
    }
}
