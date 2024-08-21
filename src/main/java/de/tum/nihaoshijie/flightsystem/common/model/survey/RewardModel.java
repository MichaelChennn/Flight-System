package de.tum.nihaoshijie.flightsystem.common.model.survey;

public class RewardModel {
    private String name;
    private String description;


    public RewardModel(String name) {
        this.name = name;
    }

    public RewardModel(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
