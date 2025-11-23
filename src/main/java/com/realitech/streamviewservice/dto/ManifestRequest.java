package com.realitech.streamviewservice.dto;

import jakarta.validation.constraints.NotBlank;

public class ManifestRequest {

    @NotBlank(message = "URL is required")
    private String url;

    public ManifestRequest() {
    }

    public ManifestRequest(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
