package com.zai.code.weather.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zai.code.weather.dto.WeatherResponse;
import com.zai.code.weather.provider.OpenWeatherProvider;
import com.zai.code.weather.provider.WeatherStackProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class WeatherServiceTest {

    @Mock
    private WeatherStackProvider weatherStackProvider;

    @Mock
    private OpenWeatherProvider openWeatherProvider;

    @Mock
    private Cache<String, WeatherResponse> weatherCache;

    @InjectMocks
    private WeatherService weatherService;

    private final String city = "melbourne";

    private WeatherResponse dummyResponse;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        dummyResponse = new WeatherResponse(25.0, 10.0);
    }

    @Test
    public void testGetWeather_returnsCachedValue() {
        when(weatherCache.getIfPresent(city)).thenReturn(dummyResponse);

        WeatherResponse result = weatherService.getWeather(city);

        assertEquals(dummyResponse, result);
        verify(weatherCache, times(1)).getIfPresent(city);
        verifyNoInteractions(weatherStackProvider, openWeatherProvider);
    }

    @Test
    public void testGetWeather_fetchesFromProviderAndCachesIt() {
        when(weatherCache.getIfPresent(city)).thenReturn(null);
        when(weatherStackProvider.getWeather(city)).thenReturn(Optional.of(dummyResponse));

        WeatherResponse result = weatherService.getWeather(city);

        assertEquals(dummyResponse, result);
        verify(weatherCache, times(1)).getIfPresent(city);
        verify(weatherCache, times(1)).put(city, dummyResponse);
        verify(weatherStackProvider, times(1)).getWeather(city);
        verify(openWeatherProvider, never()).getWeather(city);
    }

    @Test
    public void testGetWeather_fallbackToSecondaryProvider() {
        when(weatherCache.getIfPresent(city)).thenReturn(null);
        when(weatherStackProvider.getWeather(city)).thenReturn(Optional.empty());
        when(openWeatherProvider.getWeather(city)).thenReturn(Optional.of(dummyResponse));

        WeatherResponse result = weatherService.getWeather(city);

        assertEquals(dummyResponse, result);
        verify(weatherStackProvider, times(1)).getWeather(city);
        verify(openWeatherProvider, times(1)).getWeather(city);
        verify(weatherCache).put(city, dummyResponse);
    }

    @Test
    public void testGetWeather_usesStaleCacheWhenBothProvidersFail() {
        when(weatherCache.getIfPresent(city)).thenReturn(null).thenReturn(dummyResponse);
        when(weatherStackProvider.getWeather(city)).thenReturn(Optional.empty());
        when(openWeatherProvider.getWeather(city)).thenReturn(Optional.empty());

        WeatherResponse result = weatherService.getWeather(city);

        assertEquals(dummyResponse, result);
        verify(weatherCache, times(2)).getIfPresent(city);
    }

    @Test
    public void testGetWeather_allFailAndNoCache_throwsException() {
        when(weatherCache.getIfPresent(city)).thenReturn(null);
        when(weatherStackProvider.getWeather(city)).thenReturn(Optional.empty());
        when(openWeatherProvider.getWeather(city)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> weatherService.getWeather(city));
        assertEquals("All providers failed and no cached data available.", exception.getMessage());
    }

    @Test
    public void testCacheReturnsValueBeforeExpiry() {
        Cache<String, WeatherResponse> realCache = Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .build();

        weatherService = new WeatherService(weatherStackProvider, openWeatherProvider, realCache);

        realCache.put(city, dummyResponse);

        WeatherResponse result = weatherService.getWeather(city);
        assertEquals(dummyResponse, result);
    }

    @Test
    public void testCacheExpiresAfter3Seconds() throws InterruptedException {
        Cache<String, WeatherResponse> realCache = Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .build();

        weatherService = new WeatherService(weatherStackProvider, openWeatherProvider, realCache);

        realCache.put(city, dummyResponse);

        Thread.sleep(3100); // Wait slightly over 3s for expiration

        // Providers will also fail to simulate fallback to stale (but stale will be null due to expiration)
        when(weatherStackProvider.getWeather(city)).thenReturn(Optional.empty());
        when(openWeatherProvider.getWeather(city)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> weatherService.getWeather(city));
        assertEquals("All providers failed and no cached data available.", exception.getMessage());
    }

}