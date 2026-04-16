package com.microsaas.vendoriq.repository;

import com.microsaas.vendoriq.model.RenewalAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RenewalAlertRepository extends JpaRepository<RenewalAlert, UUID> {
}
