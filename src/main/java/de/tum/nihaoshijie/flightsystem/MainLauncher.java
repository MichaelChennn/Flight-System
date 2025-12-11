package de.tum.nihaoshijie.flightsystem;


import de.tum.nihaoshijie.flightsystem.client.ClientApplication;
import javafx.application.Application;
import org.springframework.boot.SpringApplication;

public class MainLauncher {
    public static void main(String[] args) {

        SpringApplication.run(FlightSystemApplication.class, args);

        Application.launch(ClientApplication.class, args);
    }
}