package com.zai.code.weather.provider;

import com.zai.code.weather.dto.WeatherResponse;
import com.zai.code.weather.dto.WeatherStackResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WeatherStackProviderTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WeatherStackProvider provider;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetWeather_successfulResponse() {
        WeatherStackResponse response = new WeatherStackResponse();
        WeatherStackResponse.Current current = new WeatherStackResponse.Current();
        current.setTemperature(25.5);
        current.setWind_speed(12.3);
        response.setCurrent(current);

        when(restTemplate.getForObject(anyString(), eq(WeatherStackResponse.class)))
                .thenReturn(response);

        Optional<WeatherResponse> result = provider.getWeather("melbourne");

        assertTrue(result.isPresent());
        assertEquals(25.5, result.get().getTemperatureDegrees());
        assertEquals(12.3, result.get().getWindSpeed());
    }

    @Test
    public void testGetWeather_nullCurrent_returnsEmpty() {
        WeatherStackResponse response = new WeatherStackResponse();
        response.setCurrent(null);

        when(restTemplate.getForObject(anyString(), eq(WeatherStackResponse.class)))
                .thenReturn(response);

        Optional<WeatherResponse> result = provider.getWeather("melbourne");

        assertFalse(result.isPresent());
    }

    @Test
    public void testGetWeather_nullResponse_returnsEmpty() {
        when(restTemplate.getForObject(anyString(), eq(WeatherStackResponse.class)))
                .thenReturn(null);

        Optional<WeatherResponse> result = provider.getWeather("melbourne");

        assertFalse(result.isPresent());
    }

    @Test
    public void testGetWeather_exception_returnsEmpty() {
        when(restTemplate.getForObject(anyString(), eq(WeatherStackResponse.class)))
                .thenThrow(new RuntimeException("API error"));

        Optional<WeatherResponse> result = provider.getWeather("melbourne");

        assertFalse(result.isPresent());
    }

    @Test
    public void testFallback_returnsEmpty() {
        Optional<WeatherResponse> fallbackResult = provider.fallback("melbourne", new RuntimeException("Fallback error"));
        assertFalse(fallbackResult.isPresent());
    }
}