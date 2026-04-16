package com.microsaas.runwaymodeler.service;

import com.microsaas.runwaymodeler.model.RunwayModel;
import com.microsaas.runwaymodeler.repository.RunwayModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RunwayModelService {
    private final RunwayModelRepository repository;

    @Transactional
    public RunwayModel createModel(String name, BigDecimal currentBurn, BigDecimal currentCash, UUID tenantId) {
        RunwayModel model = new RunwayModel();
        model.setId(UUID.randomUUID());
        model.setName(name);
        model.setCurrentBurn(currentBurn);
        model.setCurrentCash(currentCash);
        model.setRunwayDays(calculateRunwayDays(currentCash, currentBurn));
        model.setTenantId(tenantId);
        model.setUpdatedAt(LocalDateTime.now());
        return repository.save(model);
    }

    @Transactional
    public RunwayModel updateBurn(UUID id, BigDecimal newBurn, UUID tenantId) {
        RunwayModel model = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Model not found"));
        if (!model.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Model does not belong to tenant");
        }
        model.setCurrentBurn(newBurn);
        model.setRunwayDays(calculateRunwayDays(model.getCurrentCash(), newBurn));
        model.setUpdatedAt(LocalDateTime.now());
        return repository.save(model);
    }

    public Optional<RunwayModel> getModel(UUID id, UUID tenantId) {
        Optional<RunwayModel> model = repository.findById(id);
        if (model.isPresent() && !model.get().getTenantId().equals(tenantId)) {
            return Optional.empty();
        }
        return model;
    }

    public List<RunwayModel> listModels(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    private int calculateRunwayDays(BigDecimal cash, BigDecimal burn) {
        if (burn.compareTo(BigDecimal.ZERO) == 0) {
            return Integer.MAX_VALUE;
        }
        return cash.divide(burn, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(30)).intValue();
    }
}
