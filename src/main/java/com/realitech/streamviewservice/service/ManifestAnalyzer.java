package com.realitech.streamviewservice.service;

import com.realitech.streamviewservice.dto.ManifestResponse;

public interface ManifestAnalyzer {

    /**
     * Analyzes a manifest URL and returns detailed manifest information
     * @param url The manifest URL to analyze
     * @return ManifestResponse with streamtype and stream-specific details
     */
    ManifestResponse analyzeManifest(String url);
}
