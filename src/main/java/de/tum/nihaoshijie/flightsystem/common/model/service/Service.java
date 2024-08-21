package de.tum.nihaoshijie.flightsystem.common.model.service;

import java.util.HashSet;

public class Service {
    private Long id;
    private String description;
    private HashSet<String> pictureUrls;
    private String serviceType;

    public Service (Long id, String description,HashSet<String> pictureUrls, String serviceType) {
        this.id = id;
        this.description = description;
        this.pictureUrls = pictureUrls;
        this.serviceType = serviceType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashSet<String> getPictureUrls() {
        return pictureUrls;
    }

    public void setPictureUrls(HashSet<String> pictureUrls) {
        this.pictureUrls = pictureUrls;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
