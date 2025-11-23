package com.realitech.streamviewservice.service;

public interface ManifestAnalyzer {

    /**
     * Analyzes a manifest URL and returns the stream type
     * @param url The manifest URL to analyze
     * @return "hls", "dash", or "invalid"
     */
    String analyzeManifest(String url);
}
