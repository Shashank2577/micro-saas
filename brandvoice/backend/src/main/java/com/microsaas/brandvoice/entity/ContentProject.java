package com.microsaas.brandvoice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "content_projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentProject {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID tenantId;
    private String name;
    private String status;
    private String targetAudience;
    private LocalDateTime dueDate;
}
