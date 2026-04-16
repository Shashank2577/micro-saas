package com.microsaas.dependencyradar.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpgradeCandidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long dependencyId;
    private String newVersion;
    
    @ElementCollection
    private List<String> testResults;
    private String prUrl;
}
