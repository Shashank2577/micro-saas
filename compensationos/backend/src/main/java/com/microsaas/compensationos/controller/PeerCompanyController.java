package com.microsaas.compensationos.controller;

import com.microsaas.compensationos.entity.PeerCompany;
import com.microsaas.compensationos.service.PeerCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/peer-companies")
@RequiredArgsConstructor
public class PeerCompanyController {
    private final PeerCompanyService service;

    @GetMapping
    public List<PeerCompany> getAll() {
        return service.getAll();
    }

    @PostMapping
    public PeerCompany create(@RequestBody PeerCompany entity) {
        return service.save(entity);
    }
}
