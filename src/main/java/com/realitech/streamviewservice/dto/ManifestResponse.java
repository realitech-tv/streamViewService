package com.realitech.streamviewservice.dto;

public class ManifestResponse {

    private String streamtype;

    public ManifestResponse() {
    }

    public ManifestResponse(String streamtype) {
        this.streamtype = streamtype;
    }

    public String getStreamtype() {
        return streamtype;
    }

    public void setStreamtype(String streamtype) {
        this.streamtype = streamtype;
    }
}
