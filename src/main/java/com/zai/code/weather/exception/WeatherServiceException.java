package com.zai.code.weather.exception;

public class WeatherServiceException extends RuntimeException {
    public WeatherServiceException(String message) {
        super(message);
    }
}
