package com.microsaas.dataqualityai.repository;

import com.microsaas.dataqualityai.model.TestRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRunRepository extends JpaRepository<TestRun, String> {
    List<TestRun> findByTestId(String testId);
}
