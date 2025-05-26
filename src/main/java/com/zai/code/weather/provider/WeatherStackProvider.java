package com.zai.code.weather.provider;

import com.zai.code.weather.dto.WeatherResponse;
import com.zai.code.weather.dto.WeatherStackResponse;
import com.zai.code.weather.mapper.WeatherMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static java.util.Optional.ofNullable;

@Component
@RequiredArgsConstructor
public class WeatherStackProvider {

    private final RestTemplate restTemplate;

    @CircuitBreaker(name = "weatherstack", fallbackMethod = "fallback")
    public Optional<WeatherResponse> getWeather(String city) {
        String apiKey = "YOUR_API_KEY";
        String url = String.format("http://api.weatherstack.com/current?access_key=%s&query=%s", apiKey, city);
        WeatherStackResponse response = restTemplate.getForObject(url, WeatherStackResponse.class);
        return ofNullable(WeatherMapper.fromWeatherStack(response));
    }

    public Optional<WeatherResponse> fallback(String city, Throwable t) {
        return Optional.empty();
    }
}