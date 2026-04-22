package com.microsaas.callintelligence.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.callintelligence.domain.insight.ActionItem;
import com.microsaas.callintelligence.domain.insight.ActionItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ActionItemService {

    private final ActionItemRepository actionItemRepository;

    public ActionItemService(ActionItemRepository actionItemRepository) {
        this.actionItemRepository = actionItemRepository;
    }

    @Transactional(readOnly = true)
    public List<ActionItem> getActionItemsForCall(UUID callId) {
        UUID tenantId = TenantContext.require();
        return actionItemRepository.findByCallIdAndTenantId(callId, tenantId);
    }

    @Transactional(readOnly = true)
    public List<ActionItem> getAllActionItems() {
        UUID tenantId = TenantContext.require();
        return actionItemRepository.findByTenantId(tenantId);
    }
}
