package com.microsaas.realestateitel.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PropertyDto {
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String propertyType;
    private Integer squareFeet;
    private Integer bedrooms;
    private BigDecimal bathrooms;
    private Integer yearBuilt;
    private String status;
}
