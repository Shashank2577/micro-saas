package com.microsaas.educationos.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class CourseDto {
    private UUID id;
    private String title;
    private String description;
}
