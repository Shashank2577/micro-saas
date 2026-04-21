package com.microsaas.constructioniq.project;

import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class SiteService {

    private final SiteRepository siteRepository;

    public Site createSite(UUID projectId, Site site) {
        site.setProjectId(projectId);
        site.setTenantId(TenantContext.require());
        site.setCreatedAt(OffsetDateTime.now());
        site.setUpdatedAt(OffsetDateTime.now());
        return siteRepository.save(site);
    }

    @Transactional(readOnly = true)
    public List<Site> getSites(UUID projectId) {
        return siteRepository.findByProjectIdAndTenantId(projectId, TenantContext.require());
    }

    @Transactional(readOnly = true)
    public Site getSite(UUID id) {
        return siteRepository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Site not found"));
    }

    public Site updateSite(UUID id, Site siteUpdate) {
        Site site = getSite(id);
        site.setName(siteUpdate.getName());
        site.setAddress(siteUpdate.getAddress());
        site.setManagerName(siteUpdate.getManagerName());
        site.setStatus(siteUpdate.getStatus());
        site.setUpdatedAt(OffsetDateTime.now());
        return siteRepository.save(site);
    }

    public void deleteSite(UUID id) {
        Site site = getSite(id);
        siteRepository.delete(site);
    }
}
