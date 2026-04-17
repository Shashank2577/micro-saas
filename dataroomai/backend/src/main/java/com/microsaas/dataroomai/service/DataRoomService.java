package com.microsaas.dataroomai.service;

import com.microsaas.dataroomai.domain.DataRoom;
import com.microsaas.dataroomai.repository.DataRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DataRoomService {

    private final DataRoomRepository dataRoomRepository;

    @Transactional(readOnly = true)
    public List<DataRoom> getDataRooms(UUID tenantId) {
        return dataRoomRepository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public DataRoom getDataRoom(UUID id, UUID tenantId) {
        return dataRoomRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Data Room not found"));
    }

    @Transactional
    public DataRoom createDataRoom(DataRoom dataRoom) {
        return dataRoomRepository.save(dataRoom);
    }
}
