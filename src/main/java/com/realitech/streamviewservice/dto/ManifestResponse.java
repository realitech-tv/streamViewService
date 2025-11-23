package com.realitech.streamviewservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManifestResponse {

    private String streamtype;
    private HlsDetails hls;
    private DashDetails dash;

    public ManifestResponse() {
    }

    public ManifestResponse(String streamtype) {
        this.streamtype = streamtype;
    }

    public ManifestResponse(String streamtype, HlsDetails hls, DashDetails dash) {
        this.streamtype = streamtype;
        this.hls = hls;
        this.dash = dash;
    }

    public String getStreamtype() {
        return streamtype;
    }

    public void setStreamtype(String streamtype) {
        this.streamtype = streamtype;
    }

    public HlsDetails getHls() {
        return hls;
    }

    public void setHls(HlsDetails hls) {
        this.hls = hls;
    }

    public DashDetails getDash() {
        return dash;
    }

    public void setDash(DashDetails dash) {
        this.dash = dash;
    }
}
