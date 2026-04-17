package com.microsaas.dataroomai.controller;

import com.microsaas.dataroomai.domain.DataRoom;
import com.microsaas.dataroomai.service.DataRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/data-rooms")
@RequiredArgsConstructor
public class DataRoomController {

    private final DataRoomService dataRoomService;

    @GetMapping
    public ResponseEntity<List<DataRoom>> getDataRooms(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(dataRoomService.getDataRooms(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataRoom> getDataRoom(
            @PathVariable UUID id,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(dataRoomService.getDataRoom(id, tenantId));
    }

    @PostMapping
    public ResponseEntity<DataRoom> createDataRoom(
            @RequestBody DataRoom dataRoom,
            @RequestHeader("X-Tenant-ID") UUID tenantId) {
        dataRoom.setTenantId(tenantId);
        return ResponseEntity.ok(dataRoomService.createDataRoom(dataRoom));
    }
}
