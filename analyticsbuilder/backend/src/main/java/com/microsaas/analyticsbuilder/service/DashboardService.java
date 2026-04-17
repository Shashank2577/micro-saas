package com.microsaas.analyticsbuilder.service;

import com.microsaas.analyticsbuilder.dto.CreateDashboardRequest;
import com.microsaas.analyticsbuilder.dto.DashboardDto;
import com.microsaas.analyticsbuilder.dto.DashboardWidgetDto;
import com.microsaas.analyticsbuilder.dto.UpdateDashboardRequest;
import com.microsaas.analyticsbuilder.exception.ResourceNotFoundException;
import com.microsaas.analyticsbuilder.model.Dashboard;
import com.microsaas.analyticsbuilder.model.DashboardWidget;
import com.microsaas.analyticsbuilder.repository.DashboardRepository;
import com.microsaas.analyticsbuilder.repository.DashboardWidgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final DashboardRepository dashboardRepository;
    private final DashboardWidgetRepository widgetRepository;

    @Transactional(readOnly = true)
    public List<DashboardDto> getAllDashboards(String tenantId) {
        return dashboardRepository.findByTenantId(tenantId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DashboardDto getDashboard(UUID id, String tenantId) {
        Dashboard dashboard = dashboardRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Dashboard not found"));
        return mapToDto(dashboard);
    }

    @Transactional
    public DashboardDto createDashboard(CreateDashboardRequest request, String tenantId) {
        Dashboard dashboard = new Dashboard();
        dashboard.setTenantId(tenantId);
        dashboard.setName(request.getName());
        dashboard.setDescription(request.getDescription());
        dashboard.setLayoutConfig(request.getLayoutConfig());
        dashboard.setWhiteLabelConfig(request.getWhiteLabelConfig());

        Dashboard savedDashboard = dashboardRepository.save(dashboard);
        final Dashboard finalDashboard = savedDashboard;

        if (request.getWidgets() != null) {
            List<DashboardWidget> widgets = request.getWidgets().stream().map(dto -> {
                DashboardWidget widget = new DashboardWidget();
                widget.setTenantId(tenantId);
                widget.setDashboard(finalDashboard);
                widget.setType(dto.getType());
                widget.setTitle(dto.getTitle());
                widget.setConfig(dto.getConfig());
                widget.setPositionX(dto.getPositionX());
                widget.setPositionY(dto.getPositionY());
                widget.setWidth(dto.getWidth());
                widget.setHeight(dto.getHeight());
                widget.setQueryId(dto.getQueryId());
                return widget;
            }).collect(Collectors.toList());
            finalDashboard.setWidgets(widgets);
            savedDashboard = dashboardRepository.save(finalDashboard);
        }

        return mapToDto(savedDashboard);
    }

    @Transactional
    public DashboardDto updateDashboard(UUID id, UpdateDashboardRequest request, String tenantId) {
        Dashboard dashboard = dashboardRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Dashboard not found"));

        if (request.getName() != null) {
            dashboard.setName(request.getName());
        }
        if (request.getDescription() != null) {
            dashboard.setDescription(request.getDescription());
        }
        if (request.getLayoutConfig() != null) {
            dashboard.setLayoutConfig(request.getLayoutConfig());
        }
        if (request.getWhiteLabelConfig() != null) {
            dashboard.setWhiteLabelConfig(request.getWhiteLabelConfig());
        }

        if (request.getWidgets() != null) {
            dashboard.getWidgets().clear();
            List<DashboardWidget> newWidgets = request.getWidgets().stream().map(dto -> {
                DashboardWidget widget = new DashboardWidget();
                widget.setTenantId(tenantId);
                widget.setDashboard(dashboard);
                widget.setType(dto.getType());
                widget.setTitle(dto.getTitle());
                widget.setConfig(dto.getConfig());
                widget.setPositionX(dto.getPositionX());
                widget.setPositionY(dto.getPositionY());
                widget.setWidth(dto.getWidth());
                widget.setHeight(dto.getHeight());
                widget.setQueryId(dto.getQueryId());
                return widget;
            }).collect(Collectors.toList());
            dashboard.getWidgets().addAll(newWidgets);
        }

        Dashboard savedDashboard = dashboardRepository.save(dashboard);
        return mapToDto(savedDashboard);
    }

    @Transactional
    public void deleteDashboard(UUID id, String tenantId) {
        Dashboard dashboard = dashboardRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Dashboard not found"));
        dashboardRepository.delete(dashboard);
    }

    private DashboardDto mapToDto(Dashboard dashboard) {
        DashboardDto dto = new DashboardDto();
        dto.setId(dashboard.getId());
        dto.setName(dashboard.getName());
        dto.setDescription(dashboard.getDescription());
        dto.setLayoutConfig(dashboard.getLayoutConfig());
        dto.setWhiteLabelConfig(dashboard.getWhiteLabelConfig());
        dto.setCreatedAt(dashboard.getCreatedAt());
        dto.setUpdatedAt(dashboard.getUpdatedAt());
        dto.setCreatedBy(dashboard.getCreatedBy());

        if (dashboard.getWidgets() != null) {
            List<DashboardWidgetDto> widgetDtos = dashboard.getWidgets().stream().map(widget -> {
                DashboardWidgetDto wDto = new DashboardWidgetDto();
                wDto.setId(widget.getId());
                wDto.setType(widget.getType());
                wDto.setTitle(widget.getTitle());
                wDto.setConfig(widget.getConfig());
                wDto.setPositionX(widget.getPositionX());
                wDto.setPositionY(widget.getPositionY());
                wDto.setWidth(widget.getWidth());
                wDto.setHeight(widget.getHeight());
                wDto.setQueryId(widget.getQueryId());
                return wDto;
            }).collect(Collectors.toList());
            dto.setWidgets(widgetDtos);
        }

        return dto;
    }
}
