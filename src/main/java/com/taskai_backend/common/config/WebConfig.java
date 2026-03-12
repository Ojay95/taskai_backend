package com.taskai_backend.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // This maps 'http://localhost:8080/audio/**' to the physical folder
        registry.addResourceHandler("/audio/**")
                .addResourceLocations("file:uploads/audio/");
    }
}