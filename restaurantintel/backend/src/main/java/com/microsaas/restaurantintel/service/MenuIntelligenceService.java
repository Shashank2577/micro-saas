package com.microsaas.restaurantintel.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.restaurantintel.domain.MenuItem;
import com.microsaas.restaurantintel.repository.MenuItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;

@Service
@Transactional
public class MenuIntelligenceService {

    private final MenuItemRepository menuItemRepository;
    private final LiteLLMClient liteLLMClient;

    public MenuIntelligenceService(MenuItemRepository menuItemRepository, LiteLLMClient liteLLMClient) {
        this.menuItemRepository = menuItemRepository;
        this.liteLLMClient = liteLLMClient;
    }

    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findByTenantId(TenantContext.require());
    }

    public MenuItem createMenuItem(MenuItem item) {
        item.setTenantId(TenantContext.require());
        return menuItemRepository.save(item);
    }

    public String analyzeMenu() {
        List<MenuItem> items = getAllMenuItems();
        StringBuilder prompt = new StringBuilder("Analyze the following menu items based on profitability and popularity:\n");
        for (MenuItem item : items) {
            prompt.append(String.format("Item: %s, Cost: %s, Price: %s, Sold: %d\n",
                    item.getName(), item.getCost(), item.getPrice(), item.getUnitsSold()));
        }
        prompt.append("Provide strategic recommendations for each item.");

        return liteLLMClient.completeChat(prompt.toString());
    }
}
