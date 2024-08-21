package de.tum.nihaoshijie.flightsystem.common.model.survey;

import java.util.List;

public class SurveyModel {
    private String name;
    private int rate;
    private List<String> questions;
    private List<String> commands;

    public String getName() {
        return name;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
