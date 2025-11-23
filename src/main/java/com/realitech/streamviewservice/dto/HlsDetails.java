package com.realitech.streamviewservice.dto;

import java.util.List;

public class HlsDetails {

    private List<Variant> variants;

    public HlsDetails() {
    }

    public HlsDetails(List<Variant> variants) {
        this.variants = variants;
    }

    public List<Variant> getVariants() {
        return variants;
    }

    public void setVariants(List<Variant> variants) {
        this.variants = variants;
    }
}
