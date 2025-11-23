package com.realitech.streamviewservice.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManifestAnalyzerImplTest {

    private final ManifestAnalyzer manifestAnalyzer = new ManifestAnalyzerImpl();

    @Test
    void testHlsManifestWithM3u8Extension() {
        // Test with real HLS manifest URL from test data
        String hlsUrl = "https://www.radiantmediaplayer.com/media/rmp-segment/bbb-abr-aes/playlist.m3u8";
        String result = manifestAnalyzer.analyzeManifest(hlsUrl);
        assertEquals("hls", result);
    }

    @Test
    void testInvalidUrlFormat() {
        String result = manifestAnalyzer.analyzeManifest("not-a-valid-url");
        assertEquals("invalid", result);
    }

    @Test
    void testNonManifestFile() {
        String result = manifestAnalyzer.analyzeManifest("https://example.com/video.mp4");
        assertEquals("invalid", result);
    }

    @Test
    void testEmptyUrl() {
        String result = manifestAnalyzer.analyzeManifest("");
        assertEquals("invalid", result);
    }

    @Test
    void testM3u8WithQueryParameters() {
        // Test URL with query parameters
        String hlsUrl = "https://www.radiantmediaplayer.com/media/rmp-segment/bbb-abr-aes/playlist.m3u8?quality=high";
        String result = manifestAnalyzer.analyzeManifest(hlsUrl);
        assertEquals("hls", result);
    }

    @Test
    void testDashManifestWithMpdExtension() {
        // Test with real DASH manifest URL from test data
        String dashUrl = "https://storage.googleapis.com/shaka-demo-assets/sintel-widevine/dash.mpd";
        String result = manifestAnalyzer.analyzeManifest(dashUrl);
        assertEquals("dash", result);
    }

    @Test
    void testMpdWithQueryParameters() {
        // Test with BBC DASH stream (has query parameters in path)
        String dashUrl = "https://vs-cmaf-push-uk-live.akamaized.net/x=4/i=urn:bbc:pips:service:bbc_two_hd/pc_hd_abr_v2.mpd";
        String result = manifestAnalyzer.analyzeManifest(dashUrl);
        assertEquals("dash", result);
    }

    @Test
    void testNonMpdFile() {
        String result = manifestAnalyzer.analyzeManifest("https://example.com/document.xml");
        assertEquals("invalid", result);
    }
}
