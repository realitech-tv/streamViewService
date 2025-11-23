package com.realitech.streamviewservice.dto;

public class Variant {

    private Long bandwidth;
    private String codec;
    private String resolution;
    private String uri;

    public Variant() {
    }

    public Variant(Long bandwidth, String codec, String resolution, String uri) {
        this.bandwidth = bandwidth;
        this.codec = codec;
        this.resolution = resolution;
        this.uri = uri;
    }

    public Long getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(Long bandwidth) {
        this.bandwidth = bandwidth;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
