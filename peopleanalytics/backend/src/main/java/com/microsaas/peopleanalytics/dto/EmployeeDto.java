package com.microsaas.peopleanalytics.dto;

import lombok.Data;
import java.util.UUID;
import java.time.LocalDate;

@Data
public class EmployeeDto {
    private String employeeNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String role;
    private UUID managerId;
    private String status;
    private LocalDate hireDate;
}
