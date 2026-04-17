package com.microsaas.compbenchmark.repositories;

import com.microsaas.compbenchmark.model.EmployeeComp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeCompRepository extends JpaRepository<EmployeeComp, UUID> {
    Optional<EmployeeComp> findByEmployeeId(String employeeId);
}
