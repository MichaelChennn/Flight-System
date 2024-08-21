package de.tum.nihaoshijie.flightsystem.common.model;

public class PoiModel {
    private String name;
    private String type;
    private long cityId;
    private String lat;
    private String lon;
    private String address;
    private double rate;
    private String url;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public long getCityId() {
        return cityId;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getAddress() {
        return address;
    }

    public double getRate() {
        return rate;
    }

    public String getUrl() {
        return url;
    }

    public PoiModel() {
    }
}
