package com.microsaas.apievolver.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class ApiSpec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceId;
    private String version;
    
    @Enumerated(EnumType.STRING)
    private SpecType specType;
    
    @Column(columnDefinition = "TEXT")
    private String specContent;
    
    private LocalDateTime uploadedAt;

    public enum SpecType {
        OPENAPI, GRAPHQL
    }
}
