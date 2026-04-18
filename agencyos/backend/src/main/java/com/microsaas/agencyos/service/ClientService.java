package com.microsaas.agencyos.service;

import com.microsaas.agencyos.domain.Client;
import com.microsaas.agencyos.exception.ResourceNotFoundException;
import com.microsaas.agencyos.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public List<Client> getAllClients(String tenantId) {
        return clientRepository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public Client getClientById(UUID id, String tenantId) {
        return clientRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
    }

    @Transactional
    public Client createClient(Client client, String tenantId) {
        client.setTenantId(tenantId);
        return clientRepository.save(client);
    }

    @Transactional
    public Client updateClient(UUID id, Client clientDetails, String tenantId) {
        Client client = getClientById(id, tenantId);
        client.setName(clientDetails.getName());
        client.setEmail(clientDetails.getEmail());
        client.setPhone(clientDetails.getPhone());
        return clientRepository.save(client);
    }

    @Transactional
    public void deleteClient(UUID id, String tenantId) {
        Client client = getClientById(id, tenantId);
        clientRepository.delete(client);
    }
}
