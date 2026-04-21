package com.microsaas.auditvault.controller;

import com.microsaas.auditvault.model.Framework;
import com.microsaas.auditvault.service.FrameworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/frameworks")
@RequiredArgsConstructor
public class FrameworkController {
    private final FrameworkService frameworkService;

    @GetMapping
    public List<Framework> listFrameworks() {
        return frameworkService.listFrameworks();
    }
}
