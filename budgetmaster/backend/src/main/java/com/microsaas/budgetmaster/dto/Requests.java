package com.microsaas.budgetmaster.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Requests {
    @Data
    public static class CreateBudgetRequest {
        private String name;
        private BigDecimal monthlyIncome;
        private String type;
        private boolean rollingMode;
        private int month;
        private int year;
    }

    @Data
    public static class CreateCategoryRequest {
        private String name;
        private BigDecimal allocatedAmount;
        private String type;
        private boolean carryover;
    }

    @Data
    public static class UpdateCategoryRequest {
        private String name;
        private BigDecimal allocatedAmount;
        private String type;
        private boolean carryover;
    }

    @Data
    public static class CreateTransactionRequest {
        private UUID budgetId;
        private UUID categoryId;
        private BigDecimal amount;
        private LocalDate date;
        private String description;
    }

    @Data
    public static class CategorizeTransactionRequest {
        private String description;
        private BigDecimal amount;
    }

    @Data
    public static class CreateAlertRequest {
        private UUID categoryId;
        private BigDecimal thresholdPercent;
        private BigDecimal thresholdAmount;
    }

    @Data
    public static class CreateTargetRequest {
        private String name;
        private BigDecimal targetAmount;
        private LocalDate deadline;
    }

    @Data
    public static class CreateFamilyMemberRequest {
        private String name;
        private String role;
        private BigDecimal individualAllowance;
    }

    @Data
    public static class CreateIrregularExpenseRequest {
        private String name;
        private BigDecimal amount;
        private LocalDate dueDate;
        private String frequency;
    }
}
