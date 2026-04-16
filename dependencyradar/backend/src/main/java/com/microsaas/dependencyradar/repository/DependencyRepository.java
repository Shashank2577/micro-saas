package com.microsaas.dependencyradar.repository;

import com.microsaas.dependencyradar.model.Dependency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DependencyRepository extends JpaRepository<Dependency, Long> {
    List<Dependency> findByRepoId(String repoId);
}
