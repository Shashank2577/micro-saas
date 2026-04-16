package com.microsaas.invoiceprocessor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InvoiceMatchRepository extends JpaRepository<InvoiceMatch, UUID> {
}
