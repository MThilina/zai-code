package com.zai.code.weather.service;

import com.zai.code.weather.dto.WeatherResponse;
import com.zai.code.weather.provider.OpenWeatherProvider;
import com.zai.code.weather.provider.WeatherStackProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherStackProvider weatherStackProvider;
    private final OpenWeatherProvider openWeatherProvider;
    private final CacheManager cacheManager;

    private static final String CACHE_NAME = "weatherCache";

    public WeatherResponse getWeather(String city) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache == null) throw new RuntimeException("Cache not available");

        try {
            // Try to fetch from providers
            WeatherResponse fresh = fetchFromProviders(city)
                    .orElseThrow(); // will go to catch
            cache.put(city, fresh);
            return fresh;
        } catch (Exception ex) {
            // Providers failed — try cache fallback
            WeatherResponse stale = cache.get(city, WeatherResponse.class);
            if (stale != null) {
                return stale;
            } else {
                throw new RuntimeException("All providers failed and no cached data available.");
            }
        }
    }

    private Optional<WeatherResponse> fetchFromProviders(String city) {
        return weatherStackProvider.getWeather(city)
                .or(() -> openWeatherProvider.getWeather(city));
    }
}