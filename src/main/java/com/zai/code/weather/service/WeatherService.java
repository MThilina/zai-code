package com.zai.code.weather.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.zai.code.weather.dto.WeatherResponse;
import com.zai.code.weather.provider.OpenWeatherProvider;
import com.zai.code.weather.provider.WeatherStackProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherStackProvider weatherStackProvider;
    private final OpenWeatherProvider openWeatherProvider;
    private final Cache<String, WeatherResponse> weatherCache;

    public WeatherResponse getWeather(String city) {
        try {
            // If cached and still valid
            WeatherResponse cached = weatherCache.getIfPresent(city);
            if (cached != null) {
                return cached;
            }

            // Fetch from providers
            WeatherResponse fresh = fetchFromProviders(city)
                    .orElseThrow(() -> new RuntimeException("All weather providers failed"));

            weatherCache.put(city, fresh);

            return fresh;

        } catch (Exception e) {
            // Serve stale if available
            WeatherResponse stale = weatherCache.getIfPresent(city);
            if (stale != null) {
                return stale;
            }

            throw new RuntimeException("All providers failed and no cached data available.");
        }
    }

    private Optional<WeatherResponse> fetchFromProviders(String city) {
        return weatherStackProvider.getWeather(city)
                .or(() -> openWeatherProvider.getWeather(city));
    }
}