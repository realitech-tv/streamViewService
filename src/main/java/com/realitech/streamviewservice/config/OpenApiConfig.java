package com.realitech.streamviewservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI streamViewServiceAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("streamViewService API")
                        .description("RESTful web service for analyzing video stream manifests (HLS and MPEG-DASH)")
                        .version("0.1.0"));
    }
}
