package com.zai.code.weather.dto;

import lombok.Data;

@Data
public class OpenWeatherResponse {
    private Main main;
    private Wind wind;

    @Data
    public static class Main {
        private double temp; // Kelvin by default
    }

    @Data
    public static class Wind {
        private double speed;
    }
}
