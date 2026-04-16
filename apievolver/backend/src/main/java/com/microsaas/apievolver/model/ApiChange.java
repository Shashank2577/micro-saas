package com.microsaas.apievolver.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.List;

@Entity
@Data
public class ApiChange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long specId;
    private String oldVersion;
    private String newVersion;
    
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> changes;
    
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> breakingChanges;
}
