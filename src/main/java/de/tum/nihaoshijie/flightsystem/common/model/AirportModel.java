package de.tum.nihaoshijie.flightsystem.common.model;

public class AirportModel {
    private String name;
    private long cityId;
    private String iata;
    private String icao;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public String getIcao() {
        return icao;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public AirportModel() {
    }

    public AirportModel(String name, long cityId, String iata, String icao) {
        this.name = name;
        this.cityId = cityId;
        this.iata = iata;
        this.icao = icao;
    }
}
