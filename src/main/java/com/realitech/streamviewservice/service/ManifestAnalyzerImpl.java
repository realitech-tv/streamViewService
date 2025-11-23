package com.realitech.streamviewservice.service;

import com.realitech.streamviewservice.dto.DashDetails;
import com.realitech.streamviewservice.dto.HlsDetails;
import com.realitech.streamviewservice.dto.ManifestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ManifestAnalyzerImpl implements ManifestAnalyzer {

    private static final Logger logger = LoggerFactory.getLogger(ManifestAnalyzerImpl.class);
    private static final Pattern BANDWIDTH_PATTERN = Pattern.compile("#EXT-X-STREAM-INF:.*BANDWIDTH=(\\d+)");
    private final RestTemplate restTemplate;

    public ManifestAnalyzerImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public ManifestResponse analyzeManifest(String url) {
        try {
            // Validate URL format
            URI uri = new URI(url);
            String path = uri.getPath();

            // Check for HLS manifest (.m3u8 extension)
            if (path != null && path.toLowerCase().endsWith(".m3u8")) {
                String content = fetchContent(url);
                if (content != null && isValidHlsManifest(content)) {
                    HlsDetails hlsDetails = extractHlsDetails(content);
                    return new ManifestResponse("hls", hlsDetails, null);
                }
            }

            // Check for DASH manifest (.mpd extension)
            if (path != null && path.toLowerCase().endsWith(".mpd")) {
                String content = fetchContent(url);
                if (content != null && isValidDashManifest(content)) {
                    DashDetails dashDetails = new DashDetails();
                    return new ManifestResponse("dash", null, dashDetails);
                }
            }

            // If we get here, it's invalid
            return new ManifestResponse("invalid");

        } catch (URISyntaxException e) {
            logger.warn("Invalid URL syntax: {}", url);
            return new ManifestResponse("invalid");
        } catch (Exception e) {
            logger.error("Error analyzing manifest: {}", e.getMessage());
            return new ManifestResponse("invalid");
        }
    }

    private String fetchContent(String url) {
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (RestClientException e) {
            logger.warn("Failed to fetch manifest from URL: {}", url);
            return null;
        }
    }

    private boolean isValidHlsManifest(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }
        // HLS manifests must start with #EXTM3U
        return content.trim().startsWith("#EXTM3U");
    }

    private boolean isValidDashManifest(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }
        // DASH manifests are XML files with <MPD> root element
        String trimmed = content.trim();
        return trimmed.contains("<MPD") && trimmed.contains("</MPD>");
    }

    private HlsDetails extractHlsDetails(String content) {
        List<Long> variants = new ArrayList<>();

        String[] lines = content.split("\n");
        for (String line : lines) {
            Matcher matcher = BANDWIDTH_PATTERN.matcher(line);
            if (matcher.find()) {
                try {
                    long bandwidth = Long.parseLong(matcher.group(1));
                    variants.add(bandwidth);
                } catch (NumberFormatException e) {
                    logger.warn("Failed to parse bandwidth value: {}", matcher.group(1));
                }
            }
        }

        return new HlsDetails(variants);
    }
}
