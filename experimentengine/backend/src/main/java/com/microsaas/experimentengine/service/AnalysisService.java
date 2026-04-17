package com.microsaas.experimentengine.service;

import com.microsaas.experimentengine.domain.model.*;
import com.microsaas.experimentengine.domain.repository.*;
import com.crosscutting.starter.error.CcErrorCodes;
import com.crosscutting.starter.webhooks.WebhookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

@Service
public class AnalysisService {

    private final ExperimentRepository experimentRepository;
    private final VariantRepository variantRepository;
    private final MetricRepository metricRepository;
    private final AssignmentRepository assignmentRepository;
    private final EventRepository eventRepository;
    private final AnalysisResultRepository analysisResultRepository;
    private final StatsEngineService statsEngineService;
    private final WebhookService webhookService;

    // Map to keep track of peek checks for warning threshold
    private final Map<UUID, Integer> peekChecks = new HashMap<>();

    public AnalysisService(ExperimentRepository experimentRepository,
                           VariantRepository variantRepository,
                           MetricRepository metricRepository,
                           AssignmentRepository assignmentRepository,
                           EventRepository eventRepository,
                           AnalysisResultRepository analysisResultRepository,
                           StatsEngineService statsEngineService,
                           WebhookService webhookService) {
        this.experimentRepository = experimentRepository;
        this.variantRepository = variantRepository;
        this.metricRepository = metricRepository;
        this.assignmentRepository = assignmentRepository;
        this.eventRepository = eventRepository;
        this.analysisResultRepository = analysisResultRepository;
        this.statsEngineService = statsEngineService;
        this.webhookService = webhookService;
    }

    @Transactional
    public List<AnalysisResult> getResults(UUID experimentId, UUID tenantId) {
        Experiment experiment = experimentRepository.findByIdAndTenantId(experimentId, tenantId)
                .orElseThrow(() -> CcErrorCodes.resourceNotFound("Experiment not found"));

        long currentSample = assignmentRepository.countByExperimentId(experimentId);

        if (experiment.isPeekProtection() && experiment.getMinSampleSize() != null && currentSample < experiment.getMinSampleSize()) {
            int checks = peekChecks.getOrDefault(experimentId, 0) + 1;
            peekChecks.put(experimentId, checks);
            if (checks >= 5) {
                System.out.println("WARNING: Repeated peek checks before min-sample reached.");
            }
            throw CcErrorCodes.validationError("Peek protection enabled: Minimum sample size not yet reached.");
        }

        List<Variant> variants = variantRepository.findByExperimentId(experimentId);
        List<Metric> metrics = metricRepository.findByExperimentId(experimentId);

        // Recompute results for all metrics and variants
        for (Metric metric : metrics) {
            recomputeResultsForMetric(experiment, metric, variants);
        }

        return analysisResultRepository.findByExperimentId(experimentId);
    }

    @Transactional
    public void recomputeResultsForMetric(Experiment experiment, Metric metric, List<Variant> variants) {
        Variant controlVariant = variants.stream()
                .filter(v -> v.getName().equalsIgnoreCase("control"))
                .findFirst()
                .orElse(variants.get(0)); // Assume first is control if not named explicitly

        AnalysisResult controlResult = calculateResult(experiment, metric, controlVariant);
        analysisResultRepository.save(controlResult);

        for (Variant variant : variants) {
            if (variant.getId().equals(controlVariant.getId())) continue;

            AnalysisResult result = calculateResult(experiment, metric, variant);

            // Calculate statistical significance against control
            if (metric.getType() == MetricType.PROPORTION) {
                result.setPValue(statsEngineService.calculateProportionPValue(
                        controlResult.getNExposures(), controlResult.getNConversions(),
                        result.getNExposures(), result.getNConversions()
                ));

                // Prior for Beta dist: alpha=1, beta=1
                result.setBayesianProbBetter(statsEngineService.calculateBayesianProbBetter(
                        1 + result.getNConversions(), 1 + result.getNExposures() - result.getNConversions(),
                        1 + controlResult.getNConversions(), 1 + controlResult.getNExposures() - controlResult.getNConversions()
                ));
            } else {
                // Approximate variance calculation for continuous
                double varC = controlResult.getStdErr() != null ? Math.pow(controlResult.getStdErr() * Math.sqrt(controlResult.getNExposures()), 2) : 0;
                double varV = result.getStdErr() != null ? Math.pow(result.getStdErr() * Math.sqrt(result.getNExposures()), 2) : 0;

                result.setPValue(statsEngineService.calculateContinuousPValue(
                        controlResult.getMean() != null ? controlResult.getMean() : 0, varC, controlResult.getNExposures(),
                        result.getMean() != null ? result.getMean() : 0, varV, result.getNExposures()
                ));
            }

            analysisResultRepository.save(result);

            // Check guardrail violation
            if (metric.getRole() == MetricRole.GUARDRAIL) {
                if (result.getMean() != null && controlResult.getMean() != null && controlResult.getMean() > 0) {
                    double regression = (result.getMean() - controlResult.getMean()) / controlResult.getMean();
                    if (regression < -0.05) {
                        webhookService.dispatch(experiment.getTenantId(), "experiment.guardrail-violated",
                            "{\"experimentId\":\"" + experiment.getId() + "\", \"metric\":\"" + metric.getName() + "\"}");
                    }
                }
            }
        }
    }

    private AnalysisResult calculateResult(Experiment experiment, Metric metric, Variant variant) {
        AnalysisResult result = analysisResultRepository.findByExperimentIdAndMetricIdAndVariantId(
                experiment.getId(), metric.getId(), variant.getId())
                .orElseGet(() -> {
                    AnalysisResult newResult = new AnalysisResult();
                    newResult.setExperimentId(experiment.getId());
                    newResult.setMetricId(metric.getId());
                    newResult.setVariantId(variant.getId());
                    return newResult;
                });

        long nExposures = assignmentRepository.countByVariantId(variant.getId());
        result.setNExposures((int) nExposures);

        List<Event> metricEvents = eventRepository.findByExperimentIdAndEventName(experiment.getId(), metric.getEventName());
        // Filter events belonging to this variant based on assignments
        List<Event> variantEvents = metricEvents.stream().filter(e -> {
            return assignmentRepository.findByExperimentIdAndUnitId(experiment.getId(), e.getUnitId())
                .map(a -> a.getVariantId().equals(variant.getId()))
                .orElse(false);
        }).toList();

        if (metric.getType() == MetricType.PROPORTION) {
            result.setNConversions(variantEvents.size()); // Assuming 1 event = 1 conversion here for simplicity
            result.setMean(nExposures > 0 ? (double) variantEvents.size() / nExposures : 0.0);
            result.setStdErr(0.0); // handled in p-value calculation directly
        } else {
            result.setNConversions(variantEvents.size());
            if (variantEvents.isEmpty()) {
                result.setMean(0.0);
                result.setStdErr(0.0);
            } else {
                double sum = variantEvents.stream().mapToDouble(e -> e.getValue() != null ? e.getValue() : 1.0).sum();
                double mean = sum / nExposures;
                result.setMean(mean);

                double varianceSum = variantEvents.stream()
                        .mapToDouble(e -> Math.pow((e.getValue() != null ? e.getValue() : 1.0) - mean, 2))
                        .sum();
                double variance = varianceSum / (nExposures > 1 ? nExposures - 1 : 1);
                result.setStdErr(Math.sqrt(variance / nExposures));
            }
        }

        return result;
    }
}
