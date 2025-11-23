package com.realitech.streamviewservice.controller;

import com.realitech.streamviewservice.service.ManifestAnalyzer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
        when(manifestAnalyzer.analyzeManifest(anyString())).thenReturn("hls");

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.streamtype").value("hls"));
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
        when(manifestAnalyzer.analyzeManifest(anyString())).thenReturn("hls");

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.streamtype").value("hls"));
    }
}
