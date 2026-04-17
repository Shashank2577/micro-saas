package com.microsaas.apigatekeeper.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "routes")
@Data
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String tenantId;
    private String routeId;
    private String path;
    private String method;
    private String targetUrl;
    private Integer stripPrefix;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
