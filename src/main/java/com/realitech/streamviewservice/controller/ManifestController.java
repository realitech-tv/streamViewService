package com.realitech.streamviewservice.controller;

import com.realitech.streamviewservice.dto.ManifestRequest;
import com.realitech.streamviewservice.dto.ManifestResponse;
import com.realitech.streamviewservice.service.ManifestAnalyzer;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManifestController {

    private final ManifestAnalyzer manifestAnalyzer;

    public ManifestController(ManifestAnalyzer manifestAnalyzer) {
        this.manifestAnalyzer = manifestAnalyzer;
    }

    @PostMapping("/")
    public ResponseEntity<ManifestResponse> analyzeManifest(@Valid @RequestBody ManifestRequest request) {
        ManifestResponse response = manifestAnalyzer.analyzeManifest(request.getUrl());
        return ResponseEntity.ok(response);
    }
}
