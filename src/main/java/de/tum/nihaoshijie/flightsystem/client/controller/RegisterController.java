package de.tum.nihaoshijie.flightsystem.client.controller;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.jfoenix.controls.JFXButton;

import de.tum.nihaoshijie.flightsystem.client.ClientApplication;
import de.tum.nihaoshijie.flightsystem.server.persistence.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class RegisterController {
    private double x = 0, y = 0;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private JFXButton registerButton;

    @FXML
    private JFXButton backButton;

    @FXML
    private AnchorPane sideBar;

    @FXML
    private TextField textfieldAcc;

    @FXML
    private TextField textfieldEmail;

    @FXML
    private TextField textfieldPass;

    @FXML
    private DatePicker datePicker;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    /**
     * close the Programm
     * 
     * @param event the user click the close buttton
     */
    @FXML
    void closeProgram(ActionEvent event) {
        Platform.exit();
    }

    /**
     * back to the login scene
     * 
     * @param event
     */
    @FXML
    void backToLogin(ActionEvent event) {
        try {
            // change the scene to homescene
            mainPane.getScene().setRoot(FXMLLoader.load(getClass().getResource(ClientApplication.LOGIN_PATH)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * set the sidebar movebar
     */
    public void initialize() {
        sideBar.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });

        sideBar.setOnMouseDragged(mouseEvent -> {
            sideBar.getScene().getWindow().setX(mouseEvent.getScreenX() - x);
            sideBar.getScene().getWindow().setY(mouseEvent.getScreenY() - y);
        });
    }

    /**
     * register a new user
     * 
     * @param event the user click the register button
     */
    @FXML
    void userRegister(ActionEvent event) {
        if (textfieldAcc.getText().equals("") || textfieldEmail.getText().equals("")
                || textfieldPass.getText().equals("") || datePicker.getValue() == null) {
            // TODO: add the empty textfield warning
        } else {
            User tmp = new User(textfieldAcc.getText(), datePicker.getValue().toString() ,textfieldEmail.getText(), textfieldPass.getText());
            this.webClient.post()
                    .uri("user")
                    .bodyValue(tmp)
                    .retrieve()
                    .bodyToMono(User.class)
                    .onErrorStop()
                    .subscribe(t -> {
                        setUserAndOpenHomeScene(t);
                    });
        }
    }

    private void setUserAndOpenHomeScene(User user) {
        Platform.runLater(() -> {
            if (user == null) {
                // TODO: display warning user not created
                System.out.println("user can't be created!");
            } else {
                UserInfo.user = user;
                ((Stage) sideBar.getScene().getWindow()).hide();
                try {
                    ClientApplication.setStageWithoutLoginScene(new Stage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
