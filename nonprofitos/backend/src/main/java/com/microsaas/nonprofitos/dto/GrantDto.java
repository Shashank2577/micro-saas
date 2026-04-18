package com.microsaas.nonprofitos.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class GrantDto {
    private String title;
    private String funder;
    private BigDecimal amount;
    private ZonedDateTime deadline;
    private String status;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getFunder() { return funder; }
    public void setFunder(String funder) { this.funder = funder; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public ZonedDateTime getDeadline() { return deadline; }
    public void setDeadline(ZonedDateTime deadline) { this.deadline = deadline; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
