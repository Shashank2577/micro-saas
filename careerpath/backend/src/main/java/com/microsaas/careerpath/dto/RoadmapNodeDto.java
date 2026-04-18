package com.microsaas.careerpath.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoadmapNodeDto {
    private String id;
    private String type;
    private Object data; // will contain label
    private Object position; // will contain x, y
}
