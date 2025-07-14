package com.fest.backend.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173" , "https://miracle-fest-website-bimu.vercel.app" )
                        .allowedMethods("GET", "POST", "PUT","PATCH" ,  "DELETE")
                        .allowedHeaders("*")
                        .allowCredentials(true); // required when using withCredentials:true
            }
        };
    }
}
