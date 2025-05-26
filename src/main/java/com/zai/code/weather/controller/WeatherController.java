package com.zai.code.weather.controller;

import com.zai.code.weather.dto.WeatherResponse;
import com.zai.code.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/weather")
    public ResponseEntity<WeatherResponse> getWeather(
            @RequestParam(defaultValue = "melbourne") String city) {
        WeatherResponse response = weatherService.getWeather(city.toLowerCase().trim());
        return ResponseEntity.ok(response);
    }
}