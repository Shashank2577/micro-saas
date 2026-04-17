package com.microsaas.dataqualityai.repository;

import com.microsaas.dataqualityai.model.Dataset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatasetRepository extends JpaRepository<Dataset, String> {
    List<Dataset> findByTenantId(String tenantId);
}
