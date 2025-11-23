package com.realitech.streamviewservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class ManifestAnalyzerImpl implements ManifestAnalyzer {

    private static final Logger logger = LoggerFactory.getLogger(ManifestAnalyzerImpl.class);
    private final RestTemplate restTemplate;

    public ManifestAnalyzerImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String analyzeManifest(String url) {
        try {
            // Validate URL format
            URI uri = new URI(url);
            String path = uri.getPath();

            // Check for HLS manifest (.m3u8 extension)
            if (path != null && path.toLowerCase().endsWith(".m3u8")) {
                if (isValidHlsManifest(url)) {
                    return "hls";
                }
            }

            // Check for DASH manifest (.mpd extension)
            if (path != null && path.toLowerCase().endsWith(".mpd")) {
                if (isValidDashManifest(url)) {
                    return "dash";
                }
            }

            // If we get here, it's invalid
            return "invalid";

        } catch (URISyntaxException e) {
            logger.warn("Invalid URL syntax: {}", url);
            return "invalid";
        } catch (Exception e) {
            logger.error("Error analyzing manifest: {}", e.getMessage());
            return "invalid";
        }
    }

    private boolean isValidHlsManifest(String url) {
        try {
            String content = restTemplate.getForObject(url, String.class);

            if (content == null || content.trim().isEmpty()) {
                return false;
            }

            // HLS manifests must start with #EXTM3U
            return content.trim().startsWith("#EXTM3U");

        } catch (RestClientException e) {
            logger.warn("Failed to fetch manifest from URL: {}", url);
            return false;
        }
    }

    private boolean isValidDashManifest(String url) {
        try {
            String content = restTemplate.getForObject(url, String.class);

            if (content == null || content.trim().isEmpty()) {
                return false;
            }

            // DASH manifests are XML files with <MPD> root element
            String trimmed = content.trim();
            return trimmed.contains("<MPD") && trimmed.contains("</MPD>");

        } catch (RestClientException e) {
            logger.warn("Failed to fetch manifest from URL: {}", url);
            return false;
        }
    }
}
