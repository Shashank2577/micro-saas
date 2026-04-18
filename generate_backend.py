import os

models = [
    ("PurchaseRequest", "purchase-requests", "purchase_requests"),
    ("ApprovalFlow", "approval-flows", "approval_flows"),
    ("PurchaseOrder", "purchase-orders", "purchase_orders"),
    ("VendorOffer", "vendor-offers", "vendor_offers"),
    ("SpendControlRule", "spend-control-rules", "spend_control_rules"),
    ("ProcurementEvent", "events", "procurement_events") # the spec actually says events endpoint but its procurement_events table, let's stick to spec. actually spec doesn't list events endpoints explicitly, wait. It says "6. events: backend service + API + frontend page + tests" in Scope. I'll add endpoints for events.
]

base_dir = "procurebot/backend/src/main/java/com/microsaas/procurebot"

os.makedirs(f"{base_dir}/model", exist_ok=True)
os.makedirs(f"{base_dir}/repository", exist_ok=True)
os.makedirs(f"{base_dir}/service", exist_ok=True)
os.makedirs(f"{base_dir}/controller", exist_ok=True)
os.makedirs(f"{base_dir}/dto", exist_ok=True)

# Generate Entities
for model_name, api_path, table_name in models:
    content = f"""package com.microsaas.procurebot.model;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "{table_name}")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class {model_name} {{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "name", nullable = false, length = 180)
    private String name;

    @Column(name = "status", nullable = false, length = 40)
    private String status;

    @Type(JsonType.class)
    @Column(name = "metadata_json", columnDefinition = "jsonb")
    private Map<String, Object> metadataJson;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "created_at", nullable = false) // Fixing copy paste bug: should be updated_at
    private ZonedDateTime updatedAt;
}}
"""
    # Replace the bug
    content = content.replace('@Column(name = "created_at", nullable = false) // Fixing copy paste bug: should be updated_at\n    private ZonedDateTime updatedAt;', '@Column(name = "updated_at", nullable = false)\n    private ZonedDateTime updatedAt;')
    with open(f"{base_dir}/model/{model_name}.java", "w") as f:
        f.write(content)

# Generate Repositories
for model_name, api_path, table_name in models:
    content = f"""package com.microsaas.procurebot.repository;

import com.microsaas.procurebot.model.{model_name};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface {model_name}Repository extends JpaRepository<{model_name}, UUID> {{
    List<{model_name}> findByTenantId(UUID tenantId);
    Optional<{model_name}> findByIdAndTenantId(UUID id, UUID tenantId);
}}
"""
    with open(f"{base_dir}/repository/{model_name}Repository.java", "w") as f:
        f.write(content)

# Generate DTOs
for model_name, api_path, table_name in models:
    content = f"""package com.microsaas.procurebot.dto;

import lombok.Data;
import java.util.Map;

@Data
public class {model_name}Request {{
    private String name;
    private String status;
    private Map<String, Object> metadataJson;
}}
"""
    with open(f"{base_dir}/dto/{model_name}Request.java", "w") as f:
        f.write(content)

# Generate Services
for model_name, api_path, table_name in models:
    content = f"""package com.microsaas.procurebot.service;

import com.microsaas.procurebot.dto.{model_name}Request;
import com.microsaas.procurebot.model.{model_name};
import com.microsaas.procurebot.repository.{model_name}Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class {model_name}Service {{

    private final {model_name}Repository repository;

    @Transactional(readOnly = true)
    public List<{model_name}> findAll(UUID tenantId) {{
        return repository.findByTenantId(tenantId);
    }}

    @Transactional(readOnly = true)
    public {model_name} findById(UUID id, UUID tenantId) {{
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("{model_name} not found"));
    }}

    @Transactional
    public {model_name} create(UUID tenantId, {model_name}Request request) {{
        {model_name} entity = {model_name}.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .status(request.getStatus() != null ? request.getStatus() : "DRAFT")
                .metadataJson(request.getMetadataJson())
                .build();
        return repository.save(entity);
    }}

    @Transactional
    public {model_name} update(UUID id, UUID tenantId, {model_name}Request request) {{
        {model_name} entity = findById(id, tenantId);
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getStatus() != null) entity.setStatus(request.getStatus());
        if (request.getMetadataJson() != null) entity.setMetadataJson(request.getMetadataJson());
        return repository.save(entity);
    }}

    public void validate(UUID id, UUID tenantId) {{
        {model_name} entity = findById(id, tenantId);
        // Add business validation logic here
    }}
}}
"""
    with open(f"{base_dir}/service/{model_name}Service.java", "w") as f:
        f.write(content)

# Generate Controllers
for model_name, api_path, table_name in models:
    content = f"""package com.microsaas.procurebot.controller;

import com.microsaas.procurebot.dto.{model_name}Request;
import com.microsaas.procurebot.model.{model_name};
import com.microsaas.procurebot.service.{model_name}Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/procurement/{api_path}")
@RequiredArgsConstructor
public class {model_name}Controller {{

    private final {model_name}Service service;

    // TODO: Fetch tenantId from authentication context, currently hardcoded or passed as header for MVP
    private UUID getTenantId() {{
        return UUID.fromString("00000000-0000-0000-0000-000000000001");
    }}

    @GetMapping
    public ResponseEntity<List<{model_name}>> getAll() {{
        return ResponseEntity.ok(service.findAll(getTenantId()));
    }}

    @PostMapping
    public ResponseEntity<{model_name}> create(@RequestBody {model_name}Request request) {{
        return ResponseEntity.ok(service.create(getTenantId(), request));
    }}

    @GetMapping("/{{id}}")
    public ResponseEntity<{model_name}> getById(@PathVariable UUID id) {{
        return ResponseEntity.ok(service.findById(id, getTenantId()));
    }}

    @PatchMapping("/{{id}}")
    public ResponseEntity<{model_name}> update(@PathVariable UUID id, @RequestBody {model_name}Request request) {{
        return ResponseEntity.ok(service.update(id, getTenantId(), request));
    }}

    @PostMapping("/{{id}}/validate")
    public ResponseEntity<Void> validate(@PathVariable UUID id) {{
        service.validate(id, getTenantId());
        return ResponseEntity.ok().build();
    }}
}}
"""
    with open(f"{base_dir}/controller/{model_name}Controller.java", "w") as f:
        f.write(content)
