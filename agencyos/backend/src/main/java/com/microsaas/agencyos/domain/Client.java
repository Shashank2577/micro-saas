package com.microsaas.agencyos.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "clients")
@Getter
@Setter
public class Client extends BaseEntity {

    private String name;
    private String email;
    private String phone;
}
