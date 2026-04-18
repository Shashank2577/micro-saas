package com.microsaas.agencyos.controller;

import com.microsaas.agencyos.domain.Client;
import com.microsaas.agencyos.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public List<Client> getAllClients(@RequestHeader("X-Tenant-ID") String tenantId) {
        return clientService.getAllClients(tenantId);
    }

    @GetMapping("/{id}")
    public Client getClientById(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") String tenantId) {
        return clientService.getClientById(id, tenantId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Client createClient(@RequestBody Client client, @RequestHeader("X-Tenant-ID") String tenantId) {
        return clientService.createClient(client, tenantId);
    }

    @PutMapping("/{id}")
    public Client updateClient(@PathVariable UUID id, @RequestBody Client client, @RequestHeader("X-Tenant-ID") String tenantId) {
        return clientService.updateClient(id, client, tenantId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") String tenantId) {
        clientService.deleteClient(id, tenantId);
    }
}
