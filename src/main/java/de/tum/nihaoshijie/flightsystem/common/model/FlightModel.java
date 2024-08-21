package de.tum.nihaoshijie.flightsystem.common.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class FlightModel {
    private String origin;
    private String destination;
    private LocalDate flightDate;
    private LocalTime departureTime;
    private LocalTime arrivalTime;


    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(LocalDate flightDate) {
        this.flightDate = flightDate;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public FlightModel() {

    }

    // main constructor of this class
    public FlightModel(String origin, String destination, String flightDate) {
        this.origin = origin;
        this.destination = destination;
        this.flightDate = LocalDate.parse(flightDate);
    }

    public FlightModel(String origin, String destination, String flightDate, LocalTime departureTime, LocalTime arrivalTime) {
        this(origin, destination, flightDate);
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public FlightModel(String origin, String destination, LocalDate flightDate) {
        this.origin = origin;
        this.destination = destination;
        this.flightDate = flightDate;
    }
}
