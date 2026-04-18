package com.microsaas.equityintelligence.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "dilution_scenarios")
public class DilutionScenario extends BaseEntity {
}
