package de.tum.nihaoshijie.flightsystem.client.controller;

import java.util.ArrayList;
import java.util.List;

import de.tum.nihaoshijie.flightsystem.server.persistence.Flight;
import de.tum.nihaoshijie.flightsystem.server.persistence.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserInfo {
    public static User user = null;
    public static List<Flight> userFlightList;
    public static FlightProperty selectedFlight;
    public static FlightProperty selectedFlightMutiStop = null;

    public static void updateUserFlightList(List<Flight> updatedList) {
        userFlightList = updatedList;
    }
    
    public static ObservableList<String> convertUserFlightListInString() {
        if (userFlightList == null || userFlightList.size() == 0) {
            return FXCollections.observableArrayList();
        }
        List<String> result = new ArrayList<>();
        for (Flight flight : userFlightList) {
            String deptime = flight.getDepTimeEst().toString().substring(12, 16) + " ";
            String arrtime = flight.getArrTimeEst().toString().substring(12, 16) + " ";
            result.add(flight.getAirline() + ": " + deptime +  flight.getDepAirport().getIata() + " --->"
            + arrtime  + flight.getArrAirport().getIata() + " " + flight.getFlightDate().toString());
        }
        return FXCollections.observableArrayList(result);
    }

    public static User getUser() {
        return user;
    }
}
