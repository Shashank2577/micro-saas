package com.microsaas.documentvault.service;

import com.microsaas.documentvault.exception.VirusDetectedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VirusScannerService {
    public void scan(MultipartFile file) {
        if (file.getOriginalFilename() != null && file.getOriginalFilename().contains("eicar")) {
            throw new VirusDetectedException("Virus detected in file: " + file.getOriginalFilename());
        }
    }
}
