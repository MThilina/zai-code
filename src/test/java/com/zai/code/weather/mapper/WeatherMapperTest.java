package com.zai.code.weather.mapper;

import com.zai.code.weather.dto.OpenWeatherResponse;
import com.zai.code.weather.dto.WeatherResponse;
import com.zai.code.weather.dto.WeatherStackResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WeatherMapperTest {

    @Test
    public void testFromWeatherStack() {
        WeatherStackResponse.Current current = new WeatherStackResponse.Current();
        current.setTemperature(22.5);
        current.setWind_speed(11.0);

        WeatherStackResponse response = new WeatherStackResponse();
        response.setCurrent(current);

        WeatherResponse result = WeatherMapper.fromWeatherStack(response);

        assertNotNull(result);
        assertEquals(22.5, result.getTemperatureDegrees());
        assertEquals(11.0, result.getWindSpeed());
    }

    @Test
    public void testFromOpenWeather() {
        OpenWeatherResponse.Main main = new OpenWeatherResponse.Main();
        main.setTemp(298.15); // 25°C

        OpenWeatherResponse.Wind wind = new OpenWeatherResponse.Wind();
        wind.setSpeed(6.5);

        OpenWeatherResponse response = new OpenWeatherResponse();
        response.setMain(main);
        response.setWind(wind);

        WeatherResponse result = WeatherMapper.fromOpenWeather(response);

        assertNotNull(result);
        assertEquals(25.0, result.getTemperatureDegrees());
        assertEquals(6.5, result.getWindSpeed());
    }
}