package com.realitech.streamviewservice.dto;

import java.util.List;

public class HlsDetails {

    private List<Long> variants;

    public HlsDetails() {
    }

    public HlsDetails(List<Long> variants) {
        this.variants = variants;
    }

    public List<Long> getVariants() {
        return variants;
    }

    public void setVariants(List<Long> variants) {
        this.variants = variants;
    }
}
