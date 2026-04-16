package com.microsaas.apievolver.repository;

import com.microsaas.apievolver.model.ApiSpec;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface ApiSpecRepository extends JpaRepository<ApiSpec, Long> {
    Optional<ApiSpec> findByServiceIdAndVersion(String serviceId, String version);
    List<ApiSpec> findByServiceIdOrderByUploadedAtDesc(String serviceId);
}
