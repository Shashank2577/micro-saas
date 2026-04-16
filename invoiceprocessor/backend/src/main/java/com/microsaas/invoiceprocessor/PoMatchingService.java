package com.microsaas.invoiceprocessor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PoMatchingService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceMatchRepository invoiceMatchRepository;

    public PoMatchingService(PurchaseOrderRepository purchaseOrderRepository, 
                             InvoiceRepository invoiceRepository, 
                             InvoiceMatchRepository invoiceMatchRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.invoiceRepository = invoiceRepository;
        this.invoiceMatchRepository = invoiceMatchRepository;
    }

    @Transactional
    public PurchaseOrder createPo(PurchaseOrder po) {
        if (po.getStatus() == null) {
            po.setStatus(PoStatus.OPEN);
        }
        return purchaseOrderRepository.save(po);
    }

    @Transactional
    public InvoiceMatch matchInvoice(UUID invoiceId, UUID tenantId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        if (!invoice.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Invoice does not belong to tenant");
        }

        List<PurchaseOrder> openPos = purchaseOrderRepository
                .findByTenantIdAndVendorIdAndStatus(tenantId, invoice.getVendorId(), PoStatus.OPEN);

        if (openPos.isEmpty()) {
            throw new IllegalStateException("No open PO found for vendor");
        }

        BigDecimal invoiceAmount = invoice.getAmount();
        
        PurchaseOrder matchedPo = null;
        BigDecimal smallestDifference = null;
        BigDecimal validThreshold = null;

        for (PurchaseOrder po : openPos) {
            BigDecimal poAmount = po.getAmount();
            BigDecimal difference = invoiceAmount.subtract(poAmount).abs();
            BigDecimal threshold = poAmount.multiply(new BigDecimal("0.05"));
            
            if (difference.compareTo(threshold) <= 0) {
                if (smallestDifference == null || difference.compareTo(smallestDifference) < 0) {
                    smallestDifference = difference;
                    matchedPo = po;
                    validThreshold = threshold;
                }
            }
        }
        
        MatchStatus matchStatus;
        String discrepancy = null;
        PurchaseOrder poToSave = null;
        
        if (matchedPo != null) {
            matchStatus = MatchStatus.MATCHED;
            matchedPo.setStatus(PoStatus.MATCHED);
            invoice.setStatus(InvoiceStatus.MATCHED);
            poToSave = matchedPo;
        } else {
            matchStatus = MatchStatus.MISMATCH;
            poToSave = openPos.get(0); // For mismatch record context
            discrepancy = "Invoice amount " + invoiceAmount + " differs from PO amount " + poToSave.getAmount() + " by more than 5%";
        }

        purchaseOrderRepository.save(poToSave);
        invoiceRepository.save(invoice);

        InvoiceMatch match = InvoiceMatch.builder()
                .invoiceId(invoice.getId())
                .poId(poToSave.getId())
                .matchStatus(matchStatus)
                .discrepancyDescription(discrepancy)
                .tenantId(tenantId)
                .build();

        return invoiceMatchRepository.save(match);
    }

    public List<PurchaseOrder> listUnmatched(UUID tenantId) {
        return purchaseOrderRepository.findByTenantIdAndStatus(tenantId, PoStatus.OPEN);
    }
}
