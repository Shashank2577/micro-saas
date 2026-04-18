package com.marketplacehub.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PricingTier {
    private String name;
    private Double price;
    private String currency;
    private String interval;
}
