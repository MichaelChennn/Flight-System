package de.tum.nihaoshijie.flightsystem.server.business;

import de.tum.nihaoshijie.flightsystem.server.persistence.Weather;
import de.tum.nihaoshijie.flightsystem.server.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class WeatherService {
    private WeatherRepository weatherRepository;

    @Autowired
    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public Weather findWeatherByCityAndDate(long cityId, LocalDate date) {
        return weatherRepository.findWeatherByCityAndAndDate(cityId, date);
    }

}
