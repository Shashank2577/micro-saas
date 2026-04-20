package com.microsaas.cashflowanalyzer.service;

import com.microsaas.cashflowanalyzer.model.Transaction;
import com.microsaas.cashflowanalyzer.model.Category;
import com.microsaas.cashflowanalyzer.repository.TransactionRepository;
import com.microsaas.cashflowanalyzer.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class TransactionCategorizationService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionCategorizationService.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Simulate LiteLLM Categorization
    public void categorizeTransaction(Transaction transaction) {
        logger.info("Categorizing transaction: {}", transaction.getId());
        List<Category> categories = categoryRepository.findByTenantId(transaction.getTenantId());

        if (!categories.isEmpty()) {
            transaction.setCategory(categories.get(0)); // Mock logic to assign first available category
            transactionRepository.save(transaction);
        }
    }
}
