package com.zai.code.weather.dto;

import lombok.Data;

@Data
public class WeatherStackResponse {
    private Current current;

    @Data
    public static class Current {
        private double temperature;
        private double wind_speed;
    }
}
