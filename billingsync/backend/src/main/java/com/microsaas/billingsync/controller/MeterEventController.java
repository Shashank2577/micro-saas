package com.microsaas.billingsync.controller;

import com.microsaas.billingsync.model.MeterEvent;
import com.microsaas.billingsync.service.MeterEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meter-events")
public class MeterEventController {

    @Autowired
    private MeterEventService meterEventService;

    @PostMapping
    public MeterEvent recordEvent(@RequestBody MeterEvent eventRequest) {
        return meterEventService.recordEvent(eventRequest);
    }
}
