package com.microsaas.apievolver.repository;

import com.microsaas.apievolver.model.ApiChange;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApiChangeRepository extends JpaRepository<ApiChange, Long> {
    List<ApiChange> findBySpecId(Long specId);
}
