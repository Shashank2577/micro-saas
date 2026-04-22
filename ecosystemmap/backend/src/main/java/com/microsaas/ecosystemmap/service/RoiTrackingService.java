package com.microsaas.ecosystemmap.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.crosscutting.starter.webhooks.WebhookService;
import com.microsaas.ecosystemmap.entity.RoiMetric;
import com.microsaas.ecosystemmap.repository.RoiMetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoiTrackingService {

    private final RoiMetricRepository roiMetricRepository;
    private final EcosystemService ecosystemService;
    private final NodeService nodeService;
    private final WebhookService webhookService;

    public List<RoiMetric> getMetricsByEcosystem(UUID ecosystemId) {
        return roiMetricRepository.findByTenantIdAndEcosystemId(TenantContext.require().toString(), ecosystemId);
    }

    public RoiMetric getMetricById(UUID id) {
        return roiMetricRepository.findByIdAndTenantId(id, TenantContext.require().toString())
                .orElseThrow(() -> new RuntimeException("ROI Metric not found"));
    }

    @Transactional
    public RoiMetric createMetric(UUID ecosystemId, UUID nodeId, RoiMetric metric) {
        metric.setTenantId(TenantContext.require().toString());
        metric.setEcosystem(ecosystemService.getEcosystemById(ecosystemId));
        if (nodeId != null) {
            metric.setNode(nodeService.getNodeById(nodeId));
        }
        RoiMetric saved = roiMetricRepository.save(metric);
        webhookService.dispatch(TenantContext.require(), "roi.calculated", saved.getId().toString());
        return saved;
    }

    @Transactional
    public RoiMetric updateMetric(UUID id, RoiMetric updateData) {
        RoiMetric existing = getMetricById(id);
        existing.setMetricName(updateData.getMetricName());
        existing.setMetricValue(updateData.getMetricValue());
        existing.setCurrencyCode(updateData.getCurrencyCode());
        existing.setPeriod(updateData.getPeriod());
        RoiMetric updated = roiMetricRepository.save(existing);
        webhookService.dispatch(TenantContext.require(), "roi.calculated", updated.getId().toString());
        return updated;
    }

    @Transactional
    public void deleteMetric(UUID id) {
        RoiMetric existing = getMetricById(id);
        roiMetricRepository.delete(existing);
        webhookService.dispatch(TenantContext.require(), "roi_metric.deleted", existing.getId().toString());
    }
}
