package com.example.demo;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** This class contains configurations for CORS management. */
@Slf4j
@Configuration
public class CorsConfig implements WebMvcConfigurer {

  /**
   * Add CORS mappings configuration.
   *
   * @param registry CorsRegistry instance.
   */
  public void addCorsMappings(@NonNull CorsRegistry registry) {
    List<String> allowedOrigins = List.of("http://localhost:4200", "http://192.168.15.6:4200");

    log.info("CORS policy allowed origins: {}", allowedOrigins);
    log.debug("CORS policy allowed origins in debug mode: {}", allowedOrigins);

    registry
        .addMapping("/**")
        .allowedOrigins("*")
        .allowedHeaders(
            "X-XSRF-TOKEN",
            "Content-Type",
            "Accept",
            "Authorization",
            "X-Frame-Options",
            "X-XSS-Protection",
            "Content-Security-Policy")
        .allowedMethods("GET", "PUT", "POST", "DELETE", "OPTIONS", "HEAD", "PATCH");
  }
}
