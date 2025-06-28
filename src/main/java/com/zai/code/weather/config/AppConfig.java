package com.zai.code.weather.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.zai.code.weather.dto.WeatherResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(500);
        factory.setReadTimeout(500);
        return new RestTemplate(factory);
    }

    @Bean
    public com.github.benmanes.caffeine.cache.Cache<String, WeatherResponse> weatherCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .build();
    }
}