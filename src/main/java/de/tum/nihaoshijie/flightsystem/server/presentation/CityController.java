package de.tum.nihaoshijie.flightsystem.server.presentation;

import de.tum.nihaoshijie.flightsystem.common.model.PoiModel;
import de.tum.nihaoshijie.flightsystem.server.business.CityService;
import de.tum.nihaoshijie.flightsystem.server.business.PoiService;
import de.tum.nihaoshijie.flightsystem.server.business.UserService;
import de.tum.nihaoshijie.flightsystem.server.business.WeatherService;
import de.tum.nihaoshijie.flightsystem.server.persistence.City;
import de.tum.nihaoshijie.flightsystem.server.persistence.Poi;
import de.tum.nihaoshijie.flightsystem.server.persistence.User;
import de.tum.nihaoshijie.flightsystem.server.persistence.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
public class CityController {
    private CityService cityService;
    private PoiService poiService;
    private WeatherService weatherService;
    private UserService userService;

    @Autowired
    public CityController(CityService cityService, PoiService poiService, WeatherService weatherService, UserService userService) {
        this.cityService = cityService;
        this.poiService = poiService;
        this.weatherService = weatherService;
        this.userService = userService;
    }

    /**
     * City related services
     */
    @GetMapping("/city")
    public City findCity(@RequestParam("name") String name) {
        return cityService.findCityByName(name);
    }

    /**
     * Poi related services
     */
    @PostMapping("/poi")
    public void savePoi(@RequestBody PoiModel poiModel) {
        final Optional<City> city = cityService.findCityById(poiModel.getCityId());
        poiService.savePoi(new Poi(poiModel.getName(), null, city.orElse(null),
                Double.parseDouble(poiModel.getLat()), Double.parseDouble(poiModel.getLon()),
                poiModel.getAddress(), poiModel.getRate(), poiModel.getUrl()));
    }

    // save multiple poiModels in database
    // TODO: bugfix -> poi type is null!
    @PostMapping("/pois")
    public void savePois(@RequestBody PoiModel[] poiModels) {
        Arrays.stream(poiModels).forEach(poiModel -> {
            final Optional<City> city = cityService.findCityById(poiModel.getCityId());
            poiService.savePoi(new Poi(poiModel.getName(), null, city.orElse(null),
                    Double.parseDouble(poiModel.getLat()), Double.parseDouble(poiModel.getLon()),
                    poiModel.getAddress(), poiModel.getRate(), poiModel.getUrl()));
        });
    }

    @GetMapping("/pois")
    public List<Poi> findPois(@RequestParam("city") String cityName) {
        return cityService.findPoisByCity(cityName);
    }

    @PostMapping("/pois/user-list")
    public void savePoisToUserList(@RequestParam long userId, @RequestBody long[] poiIds) {
        Arrays.stream(poiIds).forEach(id -> poiService.addToUserList(userId, id));
    }

    @GetMapping("/pois/{userId}/user-list")
    public List<Poi> savePoisToUserList(@PathVariable("userId") long userId) {
        return cityService.findPoisByUserId(userId);
    }

    /**
     * Weather related services
     */
    @GetMapping("/weather")
    public Weather findWeatherByCityAndDate(@RequestParam("city") String name, @RequestParam("date") String date) {
        final City city = cityService.findCityByName(name);
        return weatherService.findWeatherByCityAndDate(city.getId(), LocalDate.parse(date));
    }
}
