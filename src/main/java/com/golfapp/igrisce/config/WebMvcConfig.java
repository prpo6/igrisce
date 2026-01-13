package com.golfapp.igrisce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // Enable trailing slash match to handle both /api/rezervacije and /api/rezervacije/
        configurer.setUseTrailingSlashMatch(true);
    }
}
