package com.microsaas.realestateitel.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.realestateitel.domain.Property;
import com.microsaas.realestateitel.dto.PropertyDto;
import com.microsaas.realestateitel.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;

    @Transactional
    public Property createProperty(PropertyDto dto) {
        UUID tenantId = TenantContext.require();
        Property property = new Property();
        property.setTenantId(tenantId);
        updatePropertyFromDto(property, dto);
        return propertyRepository.save(property);
    }

    public Property getPropertyById(UUID id) {
        return propertyRepository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Property not found"));
    }

    public List<Property> listProperties() {
        return propertyRepository.findByTenantId(TenantContext.require());
    }

    public List<Property> listPropertiesByStatus(String status) {
        return propertyRepository.findByTenantIdAndStatus(TenantContext.require(), status);
    }

    @Transactional
    public Property updateProperty(UUID id, PropertyDto dto) {
        Property property = getPropertyById(id);
        updatePropertyFromDto(property, dto);
        return propertyRepository.save(property);
    }

    private void updatePropertyFromDto(Property property, PropertyDto dto) {
        property.setAddress(dto.getAddress());
        property.setCity(dto.getCity());
        property.setState(dto.getState());
        property.setZipCode(dto.getZipCode());
        property.setPropertyType(dto.getPropertyType());
        property.setSquareFeet(dto.getSquareFeet());
        property.setBedrooms(dto.getBedrooms());
        property.setBathrooms(dto.getBathrooms());
        property.setYearBuilt(dto.getYearBuilt());
        property.setStatus(dto.getStatus());
    }
}
