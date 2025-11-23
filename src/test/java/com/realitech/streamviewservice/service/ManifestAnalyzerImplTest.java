package com.realitech.streamviewservice.service;

import com.realitech.streamviewservice.dto.ManifestResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManifestAnalyzerImplTest {

    private final ManifestAnalyzer manifestAnalyzer = new ManifestAnalyzerImpl();

    @Test
    void testHlsManifestWithM3u8Extension() {
        // Test with real HLS manifest URL from test data
        String hlsUrl = "https://www.radiantmediaplayer.com/media/rmp-segment/bbb-abr-aes/playlist.m3u8";
        ManifestResponse result = manifestAnalyzer.analyzeManifest(hlsUrl);
        assertEquals("hls", result.getStreamtype());
        assertNotNull(result.getHls());
        assertNotNull(result.getHls().getVariants());
        assertFalse(result.getHls().getVariants().isEmpty());
        assertNull(result.getDash());
    }

    @Test
    void testInvalidUrlFormat() {
        ManifestResponse result = manifestAnalyzer.analyzeManifest("not-a-valid-url");
        assertEquals("invalid", result.getStreamtype());
        assertNull(result.getHls());
        assertNull(result.getDash());
    }

    @Test
    void testNonManifestFile() {
        ManifestResponse result = manifestAnalyzer.analyzeManifest("https://example.com/video.mp4");
        assertEquals("invalid", result.getStreamtype());
        assertNull(result.getHls());
        assertNull(result.getDash());
    }

    @Test
    void testEmptyUrl() {
        ManifestResponse result = manifestAnalyzer.analyzeManifest("");
        assertEquals("invalid", result.getStreamtype());
        assertNull(result.getHls());
        assertNull(result.getDash());
    }

    @Test
    void testM3u8WithQueryParameters() {
        // Test URL with query parameters
        String hlsUrl = "https://www.radiantmediaplayer.com/media/rmp-segment/bbb-abr-aes/playlist.m3u8?quality=high";
        ManifestResponse result = manifestAnalyzer.analyzeManifest(hlsUrl);
        assertEquals("hls", result.getStreamtype());
        assertNotNull(result.getHls());
    }

    @Test
    void testDashManifestWithMpdExtension() {
        // Test with real DASH manifest URL from test data
        String dashUrl = "https://storage.googleapis.com/shaka-demo-assets/sintel-widevine/dash.mpd";
        ManifestResponse result = manifestAnalyzer.analyzeManifest(dashUrl);
        assertEquals("dash", result.getStreamtype());
        assertNotNull(result.getDash());
        assertNull(result.getHls());
    }

    @Test
    void testMpdWithQueryParameters() {
        // Test with BBC DASH stream (has query parameters in path)
        String dashUrl = "https://vs-cmaf-push-uk-live.akamaized.net/x=4/i=urn:bbc:pips:service:bbc_two_hd/pc_hd_abr_v2.mpd";
        ManifestResponse result = manifestAnalyzer.analyzeManifest(dashUrl);
        assertEquals("dash", result.getStreamtype());
        assertNotNull(result.getDash());
    }

    @Test
    void testNonMpdFile() {
        ManifestResponse result = manifestAnalyzer.analyzeManifest("https://example.com/document.xml");
        assertEquals("invalid", result.getStreamtype());
        assertNull(result.getHls());
        assertNull(result.getDash());
    }
}
