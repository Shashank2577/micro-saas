package com.microsaas.contentos;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import com.crosscutting.starter.tenancy.TenantContext;

@RestController
@RequestMapping("/api/content-items")
@RequiredArgsConstructor
public class ContentItemController {

    private final ContentItemRepository repository;

    @GetMapping
    public List<ContentItem> getAll() {
        UUID tenantId = TenantContext.require();
        return repository.findByTenantId(tenantId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentItem> getById(@PathVariable UUID id) {
        UUID tenantId = TenantContext.require();
        return repository.findByIdAndTenantId(id, tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContentItem create(@RequestBody ContentItem item) {
        UUID tenantId = TenantContext.require();
        item.setTenantId(tenantId);
        return repository.save(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContentItem> update(@PathVariable UUID id, @RequestBody ContentItem updatedItem) {
        UUID tenantId = TenantContext.require();
        return repository.findByIdAndTenantId(id, tenantId)
                .map(item -> {
                    item.setTitle(updatedItem.getTitle());
                    item.setType(updatedItem.getType());
                    item.setStatus(updatedItem.getStatus());
                    item.setSourceItemId(updatedItem.getSourceItemId());
                    item.setContent(updatedItem.getContent());
                    return ResponseEntity.ok(repository.save(item));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        UUID tenantId = TenantContext.require();
        return repository.findByIdAndTenantId(id, tenantId)
                .map(item -> {
                    repository.delete(item);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
