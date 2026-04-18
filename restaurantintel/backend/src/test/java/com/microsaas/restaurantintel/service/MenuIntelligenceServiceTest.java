package com.microsaas.restaurantintel.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.restaurantintel.domain.MenuItem;
import com.microsaas.restaurantintel.repository.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuIntelligenceServiceTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private LiteLLMClient liteLLMClient;

    @InjectMocks
    private MenuIntelligenceService menuIntelligenceService;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @Test
    void testGetAllMenuItems() {
        MenuItem item = new MenuItem();
        item.setName("Burger");
        when(menuItemRepository.findByTenantId(tenantId)).thenReturn(List.of(item));

        List<MenuItem> items = menuIntelligenceService.getAllMenuItems();
        assertEquals(1, items.size());
        assertEquals("Burger", items.get(0).getName());
    }

    @Test
    void testCreateMenuItem() {
        MenuItem item = new MenuItem();
        item.setName("Pizza");
        item.setPrice(new BigDecimal("15.00"));
        item.setCost(new BigDecimal("5.00"));

        when(menuItemRepository.save(any(MenuItem.class))).thenAnswer(i -> i.getArguments()[0]);

        MenuItem created = menuIntelligenceService.createMenuItem(item);
        assertEquals("Pizza", created.getName());
        assertEquals(tenantId, created.getTenantId());
    }
}
