package com.microsaas.cashflowanalyzer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "plaid_transaction_id")
    private String plaidTransactionId;

    private BigDecimal amount;
    private LocalDate date;
    private String name;

    @Column(name = "merchant_name")
    private String merchantName;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private Boolean pending;

    @Column(name = "is_recurring")
    private Boolean isRecurring;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Transaction() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
    public String getPlaidTransactionId() { return plaidTransactionId; }
    public void setPlaidTransactionId(String plaidTransactionId) { this.plaidTransactionId = plaidTransactionId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMerchantName() { return merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public Boolean getPending() { return pending; }
    public void setPending(Boolean pending) { this.pending = pending; }
    public Boolean getIsRecurring() { return isRecurring; }
    public void setIsRecurring(Boolean isRecurring) { this.isRecurring = isRecurring; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
