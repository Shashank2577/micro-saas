package com.microsaas.constructioniq.project;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/sites")
@RequiredArgsConstructor
public class SiteController {

    private final SiteService siteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Site createSite(@PathVariable UUID projectId, @RequestBody Site site) {
        return siteService.createSite(projectId, site);
    }

    @GetMapping
    public List<Site> getSites(@PathVariable UUID projectId) {
        return siteService.getSites(projectId);
    }

    @GetMapping("/{id}")
    public Site getSite(@PathVariable UUID id) {
        return siteService.getSite(id);
    }

    @PutMapping("/{id}")
    public Site updateSite(@PathVariable UUID id, @RequestBody Site site) {
        return siteService.updateSite(id, site);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSite(@PathVariable UUID id) {
        siteService.deleteSite(id);
    }
}
