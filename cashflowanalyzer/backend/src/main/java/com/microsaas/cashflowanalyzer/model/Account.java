package com.microsaas.cashflowanalyzer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "plaid_item_id")
    private String plaidItemId;

    @Column(name = "plaid_account_id")
    private String plaidAccountId;

    private String name;
    private String mask;
    private String type;
    private String subtype;

    @Column(name = "balance_current")
    private BigDecimal balanceCurrent;

    @Column(name = "balance_available")
    private BigDecimal balanceAvailable;

    @Column(name = "iso_currency_code")
    private String isoCurrencyCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Account() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getPlaidItemId() { return plaidItemId; }
    public void setPlaidItemId(String plaidItemId) { this.plaidItemId = plaidItemId; }
    public String getPlaidAccountId() { return plaidAccountId; }
    public void setPlaidAccountId(String plaidAccountId) { this.plaidAccountId = plaidAccountId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMask() { return mask; }
    public void setMask(String mask) { this.mask = mask; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getSubtype() { return subtype; }
    public void setSubtype(String subtype) { this.subtype = subtype; }
    public BigDecimal getBalanceCurrent() { return balanceCurrent; }
    public void setBalanceCurrent(BigDecimal balanceCurrent) { this.balanceCurrent = balanceCurrent; }
    public BigDecimal getBalanceAvailable() { return balanceAvailable; }
    public void setBalanceAvailable(BigDecimal balanceAvailable) { this.balanceAvailable = balanceAvailable; }
    public String getIsoCurrencyCode() { return isoCurrencyCode; }
    public void setIsoCurrencyCode(String isoCurrencyCode) { this.isoCurrencyCode = isoCurrencyCode; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
