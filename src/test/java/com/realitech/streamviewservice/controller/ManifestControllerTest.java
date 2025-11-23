package com.realitech.streamviewservice.controller;

import com.realitech.streamviewservice.dto.HlsDetails;
import com.realitech.streamviewservice.dto.ManifestResponse;
import com.realitech.streamviewservice.dto.Variant;
import com.realitech.streamviewservice.service.ManifestAnalyzer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ManifestController.class)
class ManifestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManifestAnalyzer manifestAnalyzer;

    @Test
    void testAnalyzeManifestWithValidUrl() throws Exception {
        String requestBody = "{\"url\":\"https://example.com/test.m3u8\"}";
        HlsDetails hlsDetails = new HlsDetails(Arrays.asList(
                new Variant(1000000L, "avc1.42c01e,mp4a.40.2", "640x360", "variant-360p.m3u8"),
                new Variant(2000000L, "avc1.42c01f,mp4a.40.2", "1280x720", "variant-720p.m3u8"),
                new Variant(5000000L, "avc1.640028,mp4a.40.2", "1920x1080", "variant-1080p.m3u8")
        ));
        ManifestResponse mockResponse = new ManifestResponse("hls", hlsDetails, null);
        when(manifestAnalyzer.analyzeManifest(anyString())).thenReturn(mockResponse);

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.streamtype").value("hls"))
                .andExpect(jsonPath("$.hls").exists())
                .andExpect(jsonPath("$.hls.variants").isArray());
    }

    @Test
    void testAnalyzeManifestWithMissingUrl() throws Exception {
        String requestBody = "{}";

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAnalyzeManifestWithEmptyUrl() throws Exception {
        String requestBody = "{\"url\":\"\"}";

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAnalyzeManifestWithQueryParameters() throws Exception {
        String requestBody = "{\"url\":\"https://example.com/test.m3u8?quality=high&bitrate=1080p\"}";
        HlsDetails hlsDetails = new HlsDetails(Arrays.asList(
                new Variant(1000000L, "avc1.42c01e,mp4a.40.2", "640x360", "variant-360p.m3u8"),
                new Variant(2000000L, "avc1.42c01f,mp4a.40.2", "1280x720", "variant-720p.m3u8")
        ));
        ManifestResponse mockResponse = new ManifestResponse("hls", hlsDetails, null);
        when(manifestAnalyzer.analyzeManifest(anyString())).thenReturn(mockResponse);

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.streamtype").value("hls"));
    }
}
