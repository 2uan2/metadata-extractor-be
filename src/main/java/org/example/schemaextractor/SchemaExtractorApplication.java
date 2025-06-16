package org.example.schemaextractor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
//@EnableDiscoveryClient
public class SchemaExtractorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchemaExtractorApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173", "https://*.pages.dev", "http://*.workers.dev", "http://test.2uan.me", "https://*2uan.me", "http://*.workers.dev", "http://*2uan.me", "http://*.2uan.me/")
//                        .allowedOrigins("")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);

//                WebMvcConfigurer.super.addCorsMappings(registry);
            }
        };
    }

}
