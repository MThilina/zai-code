package com.zai.code.weather.mapper;

import com.zai.code.weather.dto.OpenWeatherResponse;
import com.zai.code.weather.dto.WeatherResponse;
import com.zai.code.weather.dto.WeatherStackResponse;

public class WeatherMapper {

    public static WeatherResponse fromWeatherStack(WeatherStackResponse response) {
        return new WeatherResponse(
                response.getCurrent().getTemperature(),
                response.getCurrent().getWind_speed()
        );
    }

    public static WeatherResponse fromOpenWeather(OpenWeatherResponse response) {
        double tempInCelsius = response.getMain().getTemp() - 273.15;
        return new WeatherResponse(
                Math.round(tempInCelsius * 10.0) / 10.0, // rounded
                response.getWind().getSpeed()
        );
    }
}