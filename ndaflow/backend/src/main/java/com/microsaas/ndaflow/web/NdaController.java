package com.microsaas.ndaflow.web;

import com.microsaas.ndaflow.domain.Nda;
import com.microsaas.ndaflow.domain.NdaClause;
import com.microsaas.ndaflow.domain.NdaRedline;
import com.microsaas.ndaflow.repository.NdaClauseRepository;
import com.microsaas.ndaflow.repository.NdaRedlineRepository;
import com.microsaas.ndaflow.service.NdaService;
import com.microsaas.ndaflow.service.RedlineService;
import com.microsaas.ndaflow.web.dto.GenerateNdaRequest;
import com.microsaas.ndaflow.web.dto.ProposeRedlineRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NdaController {
    private final NdaService ndaService;
    private final RedlineService redlineService;
    private final NdaClauseRepository ndaClauseRepository;
    private final NdaRedlineRepository ndaRedlineRepository;

    @PostMapping("/ndas")
    @ResponseStatus(HttpStatus.CREATED)
    public Nda generateNda(@RequestBody GenerateNdaRequest request, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ndaService.generateNda(request.getTitle(), request.getCounterparty(), request.getNdaType(), tenantId);
    }

    @GetMapping("/ndas")
    public List<Nda> listNdas(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ndaService.listNdas(tenantId);
    }

    @GetMapping("/ndas/expiring")
    public List<Nda> listExpiringSoon(@RequestParam(defaultValue = "30") int days, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ndaService.listExpiringSoon(tenantId, days);
    }

    @PostMapping("/ndas/{id}/send")
    public Nda sendNda(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ndaService.sendNda(id, tenantId);
    }

    @PostMapping("/ndas/{id}/execute")
    public Nda executeNda(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ndaService.executeNda(id, tenantId);
    }

    @GetMapping("/ndas/{id}/clauses")
    public List<NdaClause> getNdaClauses(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ndaClauseRepository.findByNdaIdAndTenantId(id, tenantId);
    }

    @PostMapping("/ndas/{id}/redlines")
    @ResponseStatus(HttpStatus.CREATED)
    public NdaRedline proposeRedline(@PathVariable UUID id, @RequestBody ProposeRedlineRequest request, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return redlineService.proposeRedline(id, request.getClauseId(), request.getProposedText(), request.getRationale(), tenantId);
    }

    @GetMapping("/ndas/{id}/redlines")
    public List<NdaRedline> getNdaRedlines(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ndaRedlineRepository.findByNdaIdAndTenantId(id, tenantId);
    }

    @PostMapping("/redlines/{id}/accept")
    public NdaRedline acceptRedline(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return redlineService.acceptRedline(id, tenantId);
    }

    @PostMapping("/redlines/{id}/reject")
    public NdaRedline rejectRedline(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return redlineService.rejectRedline(id, tenantId);
    }

    @GetMapping("/redlines/{id}/suggest")
    public String suggestResponse(@PathVariable UUID id, @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return redlineService.suggestResponse(id, tenantId);
    }
}
