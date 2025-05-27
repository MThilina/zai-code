package com.zai.code.weather.provider;

import com.zai.code.weather.dto.OpenWeatherResponse;
import com.zai.code.weather.dto.WeatherResponse;
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

public class OpenWeatherProviderTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OpenWeatherProvider provider;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetWeather_successfulResponse() {
        OpenWeatherResponse mockResponse = new OpenWeatherResponse();
        OpenWeatherResponse.Main main = new OpenWeatherResponse.Main();
        main.setTemp(300.15); // Kelvin
        OpenWeatherResponse.Wind wind = new OpenWeatherResponse.Wind();
        wind.setSpeed(5.5);

        mockResponse.setMain(main);
        mockResponse.setWind(wind);

        when(restTemplate.getForObject(anyString(), eq(OpenWeatherResponse.class)))
                .thenReturn(mockResponse);

        Optional<WeatherResponse> result = provider.getWeather("melbourne");

        assertTrue(result.isPresent());
        assertEquals(27.0, result.get().getTemperatureDegrees(), 0.1);
        assertEquals(5.5, result.get().getWindSpeed(), 0.01);
    }

    @Test
    public void testGetWeather_nullMain_returnsEmpty() {
        OpenWeatherResponse response = new OpenWeatherResponse();
        response.setWind(new OpenWeatherResponse.Wind());
        when(restTemplate.getForObject(anyString(), eq(OpenWeatherResponse.class)))
                .thenReturn(response);

        Optional<WeatherResponse> result = provider.getWeather("melbourne");

        assertFalse(result.isPresent());
    }

    @Test
    public void testGetWeather_nullWind_returnsEmpty() {
        OpenWeatherResponse response = new OpenWeatherResponse();
        response.setMain(new OpenWeatherResponse.Main());
        when(restTemplate.getForObject(anyString(), eq(OpenWeatherResponse.class)))
                .thenReturn(response);

        Optional<WeatherResponse> result = provider.getWeather("melbourne");

        assertFalse(result.isPresent());
    }

    @Test
    public void testGetWeather_nullResponse_returnsEmpty() {
        when(restTemplate.getForObject(anyString(), eq(OpenWeatherResponse.class)))
                .thenReturn(null);

        Optional<WeatherResponse> result = provider.getWeather("melbourne");

        assertFalse(result.isPresent());
    }

    @Test
    public void testGetWeather_exception_returnsEmpty() {
        when(restTemplate.getForObject(anyString(), eq(OpenWeatherResponse.class)))
                .thenThrow(new RuntimeException("API error"));

        Optional<WeatherResponse> result = provider.getWeather("melbourne");

        assertFalse(result.isPresent());
    }

    @Test
    public void testFallback_returnsEmpty() {
        Optional<WeatherResponse> fallbackResult = provider.fallback("melbourne", new RuntimeException("Fail"));
        assertFalse(fallbackResult.isPresent());
    }
}