package com.marketplacehub.dto;

import lombok.Data;

import java.util.List;

@Data
public class InstallRequest {
    private String workspaceId;
    private List<String> permissions;
}
