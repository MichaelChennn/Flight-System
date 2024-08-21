package de.tum.nihaoshijie.flightsystem.common.model.service;

import java.util.HashSet;

public class Entertainment extends Multimedia{
    private String entertainmentType;

    public Entertainment(Long id, String description,
                         HashSet<String> pictureUrls,
                         String serviceType,
                         String name, String url, String entertainmentType) {
        super(id, description, pictureUrls, serviceType, name, url);
        this.entertainmentType = entertainmentType;
    }
}