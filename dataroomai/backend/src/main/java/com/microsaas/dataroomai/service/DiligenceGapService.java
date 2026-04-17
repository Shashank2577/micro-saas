package com.microsaas.dataroomai.service;

import com.microsaas.dataroomai.domain.DiligenceGap;
import com.microsaas.dataroomai.repository.DiligenceGapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DiligenceGapService {

    private final DiligenceGapRepository diligenceGapRepository;

    @Transactional(readOnly = true)
    public List<DiligenceGap> getDiligenceGaps(UUID roomId, UUID tenantId) {
        return diligenceGapRepository.findByRoomIdAndTenantId(roomId, tenantId);
    }

    @Transactional
    public DiligenceGap createDiligenceGap(DiligenceGap diligenceGap) {
        return diligenceGapRepository.save(diligenceGap);
    }
}
