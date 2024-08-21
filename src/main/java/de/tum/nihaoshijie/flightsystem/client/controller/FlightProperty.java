package de.tum.nihaoshijie.flightsystem.client.controller;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import de.tum.nihaoshijie.flightsystem.server.persistence.City;
import javafx.beans.property.SimpleStringProperty;

public class FlightProperty extends RecursiveTreeObject<FlightProperty> {
    private Long flightId;
    private SimpleStringProperty flightDate = new SimpleStringProperty();
    private SimpleStringProperty depCity = new SimpleStringProperty();
    private SimpleStringProperty arrCity = new SimpleStringProperty();
    private SimpleStringProperty depAirport = new SimpleStringProperty();
    private SimpleStringProperty depTerminal = new SimpleStringProperty();
    private SimpleStringProperty depGate = new SimpleStringProperty();
    private SimpleStringProperty depTimeEst = new SimpleStringProperty();
    private SimpleStringProperty arrAirport = new SimpleStringProperty();
    private SimpleStringProperty arrTerminal = new SimpleStringProperty();
    private SimpleStringProperty arrGate = new SimpleStringProperty();
    private SimpleStringProperty arrTimeEst = new SimpleStringProperty();
    private SimpleStringProperty airline = new SimpleStringProperty();
    private SimpleStringProperty airplane = new SimpleStringProperty();
    private SimpleStringProperty iCao = new SimpleStringProperty();
    private City depCityObj;
    private City arrCityObj;

    public FlightProperty(String flightDate, String depCity,
            String arrCity, String depAirport, String depTerminal,
            String depGate, String depTimeEst,
            String arrAirport, String arrTerminal, String arrGate,
            String arrTimeEst, String airline,
            String airplane, String iCao, Long id, City depCityObj, City arrCityObj) {

        this.flightDate.set(flightDate);
        this.depCity.set(depCity);
        this.arrCity.set(arrCity);
        this.depAirport.set(depAirport);
        this.depTerminal.set(depTerminal);
        this.depGate.set(depGate);
        this.depTimeEst.set(depTimeEst);
        this.arrAirport.set(arrAirport);
        this.arrTerminal.set(arrTerminal);
        this.arrGate.set(arrGate);
        this.arrTimeEst.set(arrTimeEst);
        this.airline.set(airline);
        this.airplane.set(airplane);
        this.iCao.set(iCao);
        this.flightId = id;
        this.depCityObj = depCityObj;
        this.arrCityObj = arrCityObj;
    }

    public SimpleStringProperty getFlightDate() {
        return flightDate;
    }

    public SimpleStringProperty getDepCity() {
        return depCity;
    }

    public SimpleStringProperty getArrCity() {
        return arrCity;
    }

    public SimpleStringProperty getDepAirport() {
        return depAirport;
    }

    public SimpleStringProperty getDepTerminal() {
        return depTerminal;
    }

    public SimpleStringProperty getDepGate() {
        return depGate;
    }

    public SimpleStringProperty getDepTimeEst() {
        return depTimeEst;
    }

    public SimpleStringProperty getArrAirport() {
        return arrAirport;
    }

    public SimpleStringProperty getArrTerminal() {
        return arrTerminal;
    }

    public SimpleStringProperty getArrGate() {
        return arrGate;
    }

    public SimpleStringProperty getArrTimeEst() {
        return arrTimeEst;
    }

    public SimpleStringProperty getAirline() {
        return airline;
    }

    public SimpleStringProperty getAirplane() {
        return airplane;
    }

    public City getDepCityObj() {
        return depCityObj;
    }

    public City getArrCityObj() {
        return arrCityObj;
    }

    public Long getId() {
        return flightId;
    }

    public SimpleStringProperty getiCao() {
        return iCao;
    }

}
