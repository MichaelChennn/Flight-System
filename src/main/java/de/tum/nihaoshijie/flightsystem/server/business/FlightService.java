package de.tum.nihaoshijie.flightsystem.server.business;

import de.tum.nihaoshijie.flightsystem.common.model.FlightModel;
import de.tum.nihaoshijie.flightsystem.server.persistence.City;
import de.tum.nihaoshijie.flightsystem.server.persistence.Flight;
import de.tum.nihaoshijie.flightsystem.server.persistence.User;
import de.tum.nihaoshijie.flightsystem.server.persistence.UserFlight;
import de.tum.nihaoshijie.flightsystem.server.repository.CityRepository;
import de.tum.nihaoshijie.flightsystem.server.repository.FlightRepository;
import de.tum.nihaoshijie.flightsystem.server.repository.UserFlightRepository;
import de.tum.nihaoshijie.flightsystem.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService {
    private FlightRepository flightRepository;
    private CityRepository cityRepository;
    private UserRepository userRepository;
    private UserFlightRepository userFlightRepository;

    @Autowired
    public FlightService(FlightRepository flightRepository, CityRepository cityRepository, UserRepository userRepository, UserFlightRepository userFlightRepository) {
        this.flightRepository = flightRepository;
        this.cityRepository = cityRepository;
        this.userRepository = userRepository;
        this.userFlightRepository = userFlightRepository;
    }

    public void addFlight(FlightModel flight) {
        City origin;
        City destination;
        // find the origin city in db and save the one if it is not existing
        final Optional<City> optionalOrigin = cityRepository.findCityByName(flight.getOrigin());
        if (optionalOrigin.isEmpty()) {
            origin = new City(flight.getOrigin());
            cityRepository.save(origin);
        } else {
            origin = optionalOrigin.orElseThrow();
        }
        // find the destination city in db and save the one if it is not existing
        final Optional<City> optionalDestination = cityRepository.findCityByName(flight.getDestination());
        if (optionalDestination.isEmpty()) {
            destination = new City(flight.getDestination());
            cityRepository.save(destination);
        } else {
            destination = optionalDestination.orElseThrow();
        }

        Flight f = new Flight(origin, destination, flight.getFlightDate());
        flightRepository.save(f);
        // TODO: 1. check if this flight is in database, if yes -> 2
        // TODO: 2. check if this flight has already been added ->
        // TODO: 3. add to user_flight
    }

    public List<Flight> findFlightsByDepCityAndArrCityAndFlightDate(String origin, String destination, LocalDate date) {
        return flightRepository.findFlightsByDepCityAndArrCityAndFlightDate(origin, destination, date);
    }

    public Optional<Flight> findFlightById(long id) {
        return flightRepository.findById(id);
    }

    public List<Flight> findAllFlights() {
        return flightRepository.findAll();
    }

    public void addFlightToUserList(User user, Flight flight) {
        userFlightRepository.save(new UserFlight(user, flight));
    }

    public List<Flight> findUserFlightsByUserId(long userId) {
        final List<Long> flightIds = userFlightRepository.findUserFlightsByUserId(userId);
        return flightRepository.findFlightsByIds(flightIds);
    }
}
