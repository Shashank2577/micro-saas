package com.microsaas.apievolver.repository;

import com.microsaas.apievolver.model.ApiDependency;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApiDependencyRepository extends JpaRepository<ApiDependency, Long> {
    List<ApiDependency> findByProviderServiceId(String providerServiceId);
    List<ApiDependency> findByConsumerServiceId(String consumerServiceId);
}
