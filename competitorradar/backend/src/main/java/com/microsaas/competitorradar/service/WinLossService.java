package com.microsaas.competitorradar.service;

import com.microsaas.competitorradar.dto.WinLossRecordDto;
import com.microsaas.competitorradar.model.WinLossRecord;
import com.microsaas.competitorradar.repository.WinLossRecordRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WinLossService {

    private final WinLossRecordRepository winLossRecordRepository;

    public List<WinLossRecordDto> getWinLossRecords() {
        UUID tenantId = UUID.fromString(TenantContext.require().toString());
        return winLossRecordRepository.findByTenantIdOrderByDateDesc(tenantId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public WinLossRecordDto recordWinLoss(WinLossRecordDto dto) {
        UUID tenantId = UUID.fromString(TenantContext.require().toString());
        WinLossRecord record = WinLossRecord.builder()
                .id(UUID.randomUUID())
                .tenantId(tenantId)
                .competitorId(dto.getCompetitorId())
                .outcome(dto.getOutcome())
                .reason(dto.getReason())
                .value(dto.getValue())
                .date(dto.getDate())
                .build();
        record = winLossRecordRepository.save(record);
        return mapToDto(record);
    }

    private WinLossRecordDto mapToDto(WinLossRecord record) {
        WinLossRecordDto dto = new WinLossRecordDto();
        dto.setId(record.getId());
        dto.setCompetitorId(record.getCompetitorId());
        dto.setOutcome(record.getOutcome());
        dto.setReason(record.getReason());
        dto.setValue(record.getValue());
        dto.setDate(record.getDate());
        return dto;
    }
}
