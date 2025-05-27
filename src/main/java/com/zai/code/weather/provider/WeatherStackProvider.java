package com.zai.code.weather.provider;

import com.zai.code.weather.dto.WeatherResponse;
import com.zai.code.weather.dto.WeatherStackResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherStackProvider {

    private final RestTemplate restTemplate;

    @CircuitBreaker(name = "weatherstack", fallbackMethod = "fallback")
    public Optional<WeatherResponse> getWeather(String city) {
        try {
            String apiKey ="YOUR_API_KEY";
            String url = "http://api.weatherstack.com/current?access_key=" + apiKey + "&query=" + city;
            WeatherStackResponse response = restTemplate.getForObject(url, WeatherStackResponse.class);

            if (response != null && response.getCurrent() != null) {
                return Optional.of(new WeatherResponse(
                        response.getCurrent().getTemperature(),
                        response.getCurrent().getWind_speed()
                ));
            } else {
                log.warn("WeatherStack returned null current block for city: {}", city);
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("WeatherStack API call failed: {}", e.getMessage());
            return Optional.empty();
        }
    }


    public Optional<WeatherResponse> fallback(String city, Throwable t) {
        return Optional.empty();
    }
}