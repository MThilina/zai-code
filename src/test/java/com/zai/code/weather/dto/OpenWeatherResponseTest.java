package com.zai.code.weather.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class OpenWeatherResponseTest {

    @Test
    public void testMainSetterGetter() {
        OpenWeatherResponse.Main main = new OpenWeatherResponse.Main();
        main.setTemp(300.15);

        assertEquals(300.15, main.getTemp());
    }

    @Test
    public void testWindSetterGetter() {
        OpenWeatherResponse.Wind wind = new OpenWeatherResponse.Wind();
        wind.setSpeed(7.3);

        assertEquals(7.3, wind.getSpeed());
    }

    @Test
    public void testOpenWeatherResponse_fullObject() {
        OpenWeatherResponse.Main main = new OpenWeatherResponse.Main();
        main.setTemp(298.65);

        OpenWeatherResponse.Wind wind = new OpenWeatherResponse.Wind();
        wind.setSpeed(5.5);

        OpenWeatherResponse response = new OpenWeatherResponse();
        response.setMain(main);
        response.setWind(wind);

        assertNotNull(response.getMain());
        assertEquals(298.65, response.getMain().getTemp());

        assertNotNull(response.getWind());
        assertEquals(5.5, response.getWind().getSpeed());
    }
}