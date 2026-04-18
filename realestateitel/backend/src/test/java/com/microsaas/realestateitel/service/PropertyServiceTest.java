package com.microsaas.realestateitel.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.realestateitel.domain.Property;
import com.microsaas.realestateitel.dto.PropertyDto;
import com.microsaas.realestateitel.repository.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;

    @InjectMocks
    private PropertyService propertyService;

    private UUID tenantId;
    private UUID propertyId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        propertyId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @Test
    void createProperty_Success() {
        PropertyDto dto = new PropertyDto();
        dto.setAddress("123 Main St");
        dto.setStatus("ACTIVE");
        
        Property property = new Property();
        property.setId(propertyId);
        property.setTenantId(tenantId);
        property.setAddress("123 Main St");

        when(propertyRepository.save(any(Property.class))).thenReturn(property);

        Property result = propertyService.createProperty(dto);

        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        assertEquals("123 Main St", result.getAddress());
        verify(propertyRepository, times(1)).save(any(Property.class));
    }

    @Test
    void getPropertyById_Success() {
        Property property = new Property();
        property.setId(propertyId);
        property.setTenantId(tenantId);

        when(propertyRepository.findByIdAndTenantId(propertyId, tenantId)).thenReturn(Optional.of(property));

        Property result = propertyService.getPropertyById(propertyId);

        assertNotNull(result);
        assertEquals(propertyId, result.getId());
    }
}
