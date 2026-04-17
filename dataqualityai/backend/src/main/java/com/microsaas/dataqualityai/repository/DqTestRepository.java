package com.microsaas.dataqualityai.repository;

import com.microsaas.dataqualityai.model.DqTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DqTestRepository extends JpaRepository<DqTest, String> {
    List<DqTest> findByDatasetId(String datasetId);
}
