package de.tum.nihaoshijie.flightsystem.client.controller;

import de.tum.nihaoshijie.flightsystem.client.ClientApplication;
import de.tum.nihaoshijie.flightsystem.client.controller.UserInfo;
import de.tum.nihaoshijie.flightsystem.server.persistence.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.time.Instant;
import java.util.Random;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

public class SurveyController {
    Random rand = new Random();

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TextField comment1;

    @FXML
    private TextField comment2;

    @FXML
    private TextField comment3;

    @FXML
    private JFXButton goBackButton;

    @FXML
    private Label question1;

    @FXML
    private Label question2;

    @FXML
    private Label question3;

    @FXML
    private JFXComboBox<Integer> rate1;

    @FXML
    private JFXComboBox<Integer> rate2;

    @FXML
    private JFXComboBox<Integer> rate3;

    @FXML
    private JFXButton sendButton;

    private Survey survey1;
    private Survey survey2;
    private Survey survey3;
    private Reward reward;
    private User user;


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() throws IOException {
        loadRandomQuestionComfort();
        loadRandomQuestionCatering();
        loadRandomQuestionEntertainment();

        iniRate(rate1);
        iniRate(rate2);
        iniRate(rate3);
    }

    void iniRate(JFXComboBox<Integer> choiceBox) {
        choiceBox.getItems().removeAll(choiceBox.getItems());
        choiceBox.getItems().addAll(1,2,3,4,5);
        choiceBox.getSelectionModel().select(5);
    }

    @FXML
    void loadRandomQuestionComfort() {
        Survey[] t = this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("survey")
                        .queryParam("theme","COMFORT")
                        .build())
                .retrieve()
                .bodyToMono(Survey[].class)
                .block();
        int n = rand.nextInt(t.length);
        survey1 = t[n];
        question1.setText(survey1.getQuestion());
    }

    @FXML
    void loadRandomQuestionCatering() {
        Survey[] t = this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("survey")
                        .queryParam("theme","CATERING")
                        .build())
                .retrieve()
                .bodyToMono(Survey[].class)
                .block();
        int n = rand.nextInt(t.length);
        survey2 = t[n];
        question2.setText(survey2.getQuestion());
    }

    @FXML
    void loadRandomQuestionEntertainment() {
        Survey[] t = this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("survey")
                        .queryParam("theme","ENTERTAINMENT")
                        .build())
                .retrieve()
                .bodyToMono(Survey[].class)
                .block();
        int n = rand.nextInt(t.length);
        survey3 = t[n];
        question3.setText(survey3.getQuestion());

    }

    @FXML
    void goBack() {
        try {
            // change the scene to homescene
            mainPane.getScene().setRoot(FXMLLoader.load(getClass().getResource(ClientApplication.HOMESCENE_PATH)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void sendAndGetReward() {
        user = findUser();
        send();
        getReward();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reward");
        alert.setContentText("Dear " + user.getUserName() + "! Thanks for your time, you got " + reward.getName() + " as a gift.");
        alert.showAndWait();

    }

    public User findUser(){
        if (UserInfo.getUser() == null) {
            return new User("AmyZ","1995-08-09","amy.zero@web.com","s*6d7KoZV3");
        } else {
            return UserInfo.getUser();
        }
    }

    void send() {
        this.webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/survey/user")
                        .queryParam("name", user.getUserName())
                        .queryParam("surveyId", survey1.getId())
                        .queryParam("rate", (double)rate1.getValue())
                        .queryParam("timeStamp", Instant.now().toString())
                        .queryParam("comment", comment1.getText())
                        .build())
                .retrieve()
                .bodyToMono(UserSurvey.class)
                .block();

        this.webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/survey/user")
                        .queryParam("name", user.getUserName())
                        .queryParam("surveyId", survey2.getId())
                        .queryParam("rate", (double)rate2.getValue())
                        .queryParam("timeStamp", Instant.now().toString())
                        .queryParam("comment", comment2.getText())
                        .build())
                .retrieve()
                .bodyToMono(UserSurvey.class)
                .block();

        this.webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/survey/user")
                        .queryParam("name", user.getUserName())
                        .queryParam("surveyId", survey3.getId())
                        .queryParam("rate", (double)rate3.getValue())
                        .queryParam("timeStamp", Instant.now().toString())
                        .queryParam("comment", comment3.getText())
                        .build())
                .retrieve()
                .bodyToMono(UserSurvey.class)
                .block();
    }

    void getReward(){
        Reward[] r = this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/reward/all")
                        .build())
                .retrieve()
                .bodyToMono(Reward[].class)
                .block();

        int n = rand.nextInt(r.length);
        reward = r[n];

        this.webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/reward/user")
                        .queryParam("name",user.getUserName())
                        .queryParam("rewardName",reward.getName())
                        .queryParam("timeStamp",Instant.now().toString())
                        .build())
                .retrieve()
                .bodyToMono(UserReward.class)
                .block();
    }
}
