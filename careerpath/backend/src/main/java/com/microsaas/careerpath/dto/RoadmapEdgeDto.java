package com.microsaas.careerpath.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoadmapEdgeDto {
    private String id;
    private String source;
    private String target;
    private boolean animated;
}
