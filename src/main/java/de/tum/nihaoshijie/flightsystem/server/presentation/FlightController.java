package de.tum.nihaoshijie.flightsystem.server.presentation;

import de.tum.nihaoshijie.flightsystem.server.business.FlightService;
import de.tum.nihaoshijie.flightsystem.server.business.UserService;
import de.tum.nihaoshijie.flightsystem.server.persistence.Flight;
import de.tum.nihaoshijie.flightsystem.server.persistence.User;
import de.tum.nihaoshijie.flightsystem.server.persistence.UserFlight;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import de.tum.nihaoshijie.flightsystem.common.model.FlightModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class FlightController {
    private FlightService flightService;
    private UserService userService;

    @Autowired
    public FlightController(FlightService flightService, UserService userService) {
        this.flightService = flightService;
        this.userService = userService;
    }

    @GetMapping("/flight")
    public List<Flight> findFlightsByOriginAndDestinationAndDate(@RequestParam("origin") String origin,
                                                                  @RequestParam("destination") String destination,
                                                                  @RequestParam("date") String flightDate) {
        return flightService.findFlightsByDepCityAndArrCityAndFlightDate(origin, destination, LocalDate.parse(flightDate));
    }

    @GetMapping("/flight/all")
    public List<Flight> findAllFlights() {
        return flightService.findAllFlights();
    }

    // for test use, client doesn't need it
    @PostMapping("/flight")
    public void saveFlight(@RequestBody FlightModel flight) {
        flightService.addFlight(flight);
    }

    @PostMapping("/flight/user-list")
    public void saveFlight(@RequestParam("userId") long userId, @RequestParam("flightId") long flightId) {
        final Optional<User> user = userService.findUserById(userId);
        if(user.isEmpty()) {
            throw new IllegalArgumentException("User id is not found in database");
        }
        final Optional<Flight> flight = flightService.findFlightById(flightId);
        if(flight.isEmpty()) {
            throw new IllegalArgumentException("Flight id is not found in database");
        }
        flightService.addFlightToUserList(user.orElse(null), flight.orElse(null));
    }

    @GetMapping("/flight/user-list")
    public List<Flight> findUserFlights(@RequestParam("userId") long userId) {
        return flightService.findUserFlightsByUserId(userId);
    }
}
