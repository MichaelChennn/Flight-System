package de.tum.nihaoshijie.flightsystem.common.model.service;

import java.util.HashSet;

public class Instruction extends Multimedia{
    private String instrctionType;

    public Instruction(Long id, String description,
                       HashSet<String> pictureUrls,
                       String serviceType, String name,
                       String url, String instrctionType) {
        super(id, description,pictureUrls, serviceType, name, url);
        this.instrctionType = instrctionType;
    }
}
