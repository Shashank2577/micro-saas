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
public class Dependency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String repoId;
    private String name;
    private String currentVersion;
    private String latestVersion;
    private boolean outdated;
    private boolean deprecated;

    @OneToMany(mappedBy = "dependency", cascade = CascadeType.ALL)
    private List<Vulnerability> vulnerabilities;
}
