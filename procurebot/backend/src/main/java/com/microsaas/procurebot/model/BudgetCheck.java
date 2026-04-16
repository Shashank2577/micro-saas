package com.microsaas.procurebot.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "budget_checks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetCheck {

    @Id
    private UUID id;
    
    private UUID requestId;
    
    private String category;
    
    private BigDecimal requestedAmount;
    
    private BigDecimal availableBudget;
    
    private boolean passed;
    
    private UUID tenantId;
}
