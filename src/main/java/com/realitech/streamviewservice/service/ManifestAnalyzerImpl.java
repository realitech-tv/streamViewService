package com.realitech.streamviewservice.service;

import com.realitech.streamviewservice.dto.DashDetails;
import com.realitech.streamviewservice.dto.HlsDetails;
import com.realitech.streamviewservice.dto.ManifestResponse;
import com.realitech.streamviewservice.dto.Variant;
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
    private static final Pattern STREAM_INF_PATTERN = Pattern.compile("#EXT-X-STREAM-INF:(.*)");
    private static final Pattern BANDWIDTH_PATTERN = Pattern.compile("BANDWIDTH=(\\d+)");
    private static final Pattern CODECS_PATTERN = Pattern.compile("CODECS=\"([^\"]+)\"");
    private static final Pattern RESOLUTION_PATTERN = Pattern.compile("RESOLUTION=(\\d+x\\d+)");
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
        List<Variant> variants = new ArrayList<>();

        String[] lines = content.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            Matcher streamInfMatcher = STREAM_INF_PATTERN.matcher(line);
            if (streamInfMatcher.find()) {
                String attributes = streamInfMatcher.group(1);

                Long bandwidth = null;
                String codec = null;
                String resolution = null;
                String uri = null;

                // Extract BANDWIDTH
                Matcher bandwidthMatcher = BANDWIDTH_PATTERN.matcher(attributes);
                if (bandwidthMatcher.find()) {
                    try {
                        bandwidth = Long.parseLong(bandwidthMatcher.group(1));
                    } catch (NumberFormatException e) {
                        logger.warn("Failed to parse bandwidth value: {}", bandwidthMatcher.group(1));
                    }
                }

                // Extract CODECS
                Matcher codecsMatcher = CODECS_PATTERN.matcher(attributes);
                if (codecsMatcher.find()) {
                    codec = codecsMatcher.group(1);
                }

                // Extract RESOLUTION
                Matcher resolutionMatcher = RESOLUTION_PATTERN.matcher(attributes);
                if (resolutionMatcher.find()) {
                    resolution = resolutionMatcher.group(1);
                }

                // Extract URI (next line after #EXT-X-STREAM-INF)
                if (i + 1 < lines.length) {
                    String nextLine = lines[i + 1].trim();
                    if (!nextLine.startsWith("#")) {
                        uri = nextLine;
                    }
                }

                // Only add variant if we have at least bandwidth
                if (bandwidth != null) {
                    variants.add(new Variant(bandwidth, codec, resolution, uri));
                }
            }
        }

        return new HlsDetails(variants);
    }
}
