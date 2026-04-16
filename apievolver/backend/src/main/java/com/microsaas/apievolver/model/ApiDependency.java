package com.microsaas.apievolver.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.List;

@Entity
@Data
public class ApiDependency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String consumerServiceId;
    private String providerServiceId;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> usedEndpoints;

    private boolean sensitiveToBreakingChanges;
}
