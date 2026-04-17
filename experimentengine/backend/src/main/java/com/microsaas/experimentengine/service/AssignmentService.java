package com.microsaas.experimentengine.service;

import com.microsaas.experimentengine.domain.model.Assignment;
import com.microsaas.experimentengine.domain.model.Experiment;
import com.microsaas.experimentengine.domain.model.Variant;
import com.microsaas.experimentengine.domain.repository.AssignmentRepository;
import com.microsaas.experimentengine.domain.repository.ExperimentRepository;
import com.microsaas.experimentengine.domain.repository.VariantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.crosscutting.starter.error.CcErrorCodes;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final ExperimentRepository experimentRepository;
    private final VariantRepository variantRepository;

    public AssignmentService(AssignmentRepository assignmentRepository,
                             ExperimentRepository experimentRepository,
                             VariantRepository variantRepository) {
        this.assignmentRepository = assignmentRepository;
        this.experimentRepository = experimentRepository;
        this.variantRepository = variantRepository;
    }

    @Transactional
    public Variant assign(UUID experimentId, String unitId, UUID tenantId) {
        Experiment experiment = experimentRepository.findByIdAndTenantId(experimentId, tenantId)
                .orElseThrow(() -> CcErrorCodes.resourceNotFound("Experiment not found"));

        Optional<Assignment> existingAssignment = assignmentRepository.findByExperimentIdAndUnitId(experimentId, unitId);
        if (existingAssignment.isPresent()) {
            return variantRepository.findById(existingAssignment.get().getVariantId())
                    .orElseThrow(() -> CcErrorCodes.internalError("Variant not found for existing assignment"));
        }

        List<Variant> variants = variantRepository.findByExperimentId(experimentId);
        if (variants.isEmpty()) {
            throw CcErrorCodes.validationError("Experiment has no variants configured");
        }

        int hashBucket = getHashBucket(experimentId.toString() + unitId);

        double cumulativePercent = 0.0;
        Variant assignedVariant = variants.get(0);

        for (Variant variant : variants) {
            cumulativePercent += variant.getTrafficPercent();
            if (hashBucket < cumulativePercent) {
                assignedVariant = variant;
                break;
            }
        }

        Assignment assignment = new Assignment();
        assignment.setExperimentId(experimentId);
        assignment.setUnitId(unitId);
        assignment.setVariantId(assignedVariant.getId());
        assignmentRepository.save(assignment);

        return assignedVariant;
    }

    private int getHashBucket(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            // Take first 4 bytes to form an integer
            int hashValue = ((encodedhash[0] & 0xFF) << 24) |
                            ((encodedhash[1] & 0xFF) << 16) |
                            ((encodedhash[2] & 0xFF) << 8) |
                            (encodedhash[3] & 0xFF);
            return Math.abs(hashValue % 100);
        } catch (NoSuchAlgorithmException e) {
            throw CcErrorCodes.internalError("Hashing algorithm not available");
        }
    }
}
