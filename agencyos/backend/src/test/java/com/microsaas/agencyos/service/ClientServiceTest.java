package com.microsaas.agencyos.service;

import com.microsaas.agencyos.domain.Client;
import com.microsaas.agencyos.exception.ResourceNotFoundException;
import com.microsaas.agencyos.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createClient() {
        Client client = new Client();
        client.setName("Test Client");
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        Client created = clientService.createClient(client, "tenant-1");

        assertNotNull(created);
        assertEquals("tenant-1", client.getTenantId());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void getClientById() {
        UUID id = UUID.randomUUID();
        Client client = new Client();
        client.setId(id);
        when(clientRepository.findByIdAndTenantId(id, "tenant-1")).thenReturn(Optional.of(client));

        Client found = clientService.getClientById(id, "tenant-1");

        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @Test
    void getClientById_NotFound() {
        UUID id = UUID.randomUUID();
        when(clientRepository.findByIdAndTenantId(id, "tenant-1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.getClientById(id, "tenant-1"));
    }
}
