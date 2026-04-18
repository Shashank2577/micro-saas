package com.microsaas.restaurantintel.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.restaurantintel.domain.Ingredient;
import com.microsaas.restaurantintel.domain.PredictiveOrder;
import com.microsaas.restaurantintel.repository.IngredientRepository;
import com.microsaas.restaurantintel.repository.PredictiveOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PredictiveOrderingServiceTest {

    @Mock
    private PredictiveOrderRepository orderRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private LiteLLMClient liteLLMClient;

    @InjectMocks
    private PredictiveOrderingService predictiveOrderingService;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @Test
    void testGetIngredients() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Tomato");
        when(ingredientRepository.findByTenantId(tenantId)).thenReturn(List.of(ingredient));

        List<Ingredient> items = predictiveOrderingService.getIngredients();
        assertEquals(1, items.size());
        assertEquals("Tomato", items.get(0).getName());
    }

    @Test
    void testUpdateOrderStatus() {
        UUID orderId = UUID.randomUUID();
        PredictiveOrder order = new PredictiveOrder();
        order.setStatus("PENDING");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(PredictiveOrder.class))).thenAnswer(i -> i.getArguments()[0]);

        PredictiveOrder updated = predictiveOrderingService.updateOrderStatus(orderId, "APPROVED");
        assertEquals("APPROVED", updated.getStatus());
    }
}
