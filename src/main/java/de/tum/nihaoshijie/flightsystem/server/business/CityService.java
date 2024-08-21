package de.tum.nihaoshijie.flightsystem.server.business;

import de.tum.nihaoshijie.flightsystem.server.persistence.City;
import de.tum.nihaoshijie.flightsystem.server.persistence.Poi;
import de.tum.nihaoshijie.flightsystem.server.repository.CityRepository;
import de.tum.nihaoshijie.flightsystem.server.repository.PoiRepository;
import de.tum.nihaoshijie.flightsystem.server.repository.UserPoiRepository;
import de.tum.nihaoshijie.flightsystem.server.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityService {
    private CityRepository cityRepository;
    private PoiRepository poiRepository;
    private WeatherRepository weatherRepository;
    private UserPoiRepository userPoiRepository;

    @Autowired
    public CityService(CityRepository cityRepository, PoiRepository poiRepository, WeatherRepository weatherRepository, UserPoiRepository userPoiRepository) {
        this.cityRepository = cityRepository;
        this.poiRepository = poiRepository;
        this.weatherRepository = weatherRepository;
        this.userPoiRepository = userPoiRepository;
    }

    public City findCityByName(String name) {
        return cityRepository.findCityByName(name).orElse(null);
    }

    // The method is used to insert data with joined columns of city in other tables.
    public Optional<City> findCityById(long id) {
        return cityRepository.findById(id);
    }

    public List<Poi> findPoisByCity(String name) {
        final Optional<City> city = cityRepository.findCityByName(name);
        if(city.isEmpty()) {
            return null;
        }
        return poiRepository.findPoisByCity(city.get().getId());
    }

    public List<Poi> findPoisByUserId(long userId) {
        return userPoiRepository.findPoisByUserId(userId);
    }


}
