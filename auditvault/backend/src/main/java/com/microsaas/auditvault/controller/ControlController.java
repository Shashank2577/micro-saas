package com.microsaas.auditvault.controller;

import com.microsaas.auditvault.model.Control;
import com.microsaas.auditvault.service.ControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/frameworks/{frameworkId}/controls")
@RequiredArgsConstructor
public class ControlController {
    private final ControlService controlService;

    @GetMapping
    public List<Control> listControls(@PathVariable UUID frameworkId) {
        return controlService.listControls(frameworkId);
    }
}
