package de.tum.nihaoshijie.flightsystem.client.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController implements Initializable {
    private double x = 0, y = 0;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private JFXButton loginButton;

    @FXML
    private JFXButton registerButton;

    @FXML
    private AnchorPane sideBar;

    @FXML
    private TextField textfieldAcc;

    @FXML
    private TextField textfieldPass;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    @FXML
    void closeProgram(ActionEvent event) {
        Platform.exit();
    }

    /**
     * Initialize the Login Scene and make it movable with the mouse
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sideBar.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });

        sideBar.setOnMouseDragged(mouseEvent -> {
            sideBar.getScene().getWindow().setX(mouseEvent.getScreenX() - x);
            sideBar.getScene().getWindow().setY(mouseEvent.getScreenY() - y);
        });
    }

    @FXML
    void userLogin(ActionEvent event) {
        if (textfieldAcc.getText().equals("") || textfieldPass.getText().equals("")) {
            ClientApplication.displayWarining( "Empty content", "Please enter your account or passwort again!", mainPane.getScene().getWindow());
        } else {
            User user = this.webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("user")
                            .queryParam("userName", textfieldAcc.getText())
                            .build())
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();
            
        try {
            setUserAndOpenHomeScene(user);
        } catch (IOException e) {
            e.printStackTrace();
        }        

        }

    }

    /**
     * change the scene to register scene
     */
    @FXML
    void userRegister(ActionEvent event) {
        try {
            mainPane.getScene().setRoot(FXMLLoader.load(getClass().getResource(ClientApplication.REGISTER_PATH)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Set the userId and change the scene to homescene when login successful
     * 
     * @param user get form the server site
     */
    private void setUserAndOpenHomeScene(User user) throws IOException {
        Platform.runLater(() -> {
            if (user == null) {
                ClientApplication.displayWarining( "User not found", "Please check your username again!", mainPane.getScene().getWindow());
            } else {
                if (user.getPassword().equals(textfieldPass.getText())) {
                    UserInfo.user = user;
                    ((Stage) sideBar.getScene().getWindow()).hide();
                    try {
                        ClientApplication.setStageWithoutLoginScene(new Stage());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    System.out.println("wrong passwort");
                    ClientApplication.displayWarining("Wrong Passwort", "Please check your passwort again!", mainPane.getScene().getWindow());
                }

            }
        });

    }

}
