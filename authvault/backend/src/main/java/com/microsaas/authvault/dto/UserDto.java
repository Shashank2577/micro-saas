package com.microsaas.authvault.dto;

import lombok.Data;

@Data
public class UserDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
