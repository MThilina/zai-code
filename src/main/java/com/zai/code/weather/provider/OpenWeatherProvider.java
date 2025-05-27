package com.zai.code.weather.provider;

import com.zai.code.weather.dto.OpenWeatherResponse;
import com.zai.code.weather.dto.WeatherResponse;
import com.zai.code.weather.mapper.WeatherMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import lombok.extern.slf4j.Slf4j;


@Component
@RequiredArgsConstructor
@Slf4j
public class OpenWeatherProvider {

    private final RestTemplate restTemplate;

    @CircuitBreaker(name = "openweather", fallbackMethod = "fallback")
    public Optional<WeatherResponse> getWeather(String city) {
        try {
            String apiKey = "2326504fb9b100bee21400190e4dbe6d";
            String url = String.format("http://api.openweathermap.org/data/2.5/weather?q=%s,AU&appid=%s", city, apiKey);
            OpenWeatherResponse response = restTemplate.getForObject(url, OpenWeatherResponse.class);

            if (response != null && response.getMain() != null && response.getWind() != null) {
                return ofNullable(WeatherMapper.fromOpenWeather(response));
            } else {
                log.warn("OpenWeather response missing fields: main={}, wind={}",
                        response != null ? response.getMain() : null,
                        response != null ? response.getWind() : null);
                return Optional.empty();
            }

        } catch (Exception e) {
            log.error("OpenWeather API error for city {}: {}", city, e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<WeatherResponse> fallback(String city, Throwable t) {
        log.warn("Fallback triggered for OpenWeather API for city {}: {}", city, t.getMessage());
        return Optional.empty();
    }
}

