package de.tum.nihaoshijie.flightsystem.client.controller;

import de.tum.nihaoshijie.flightsystem.client.ClientApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


public class HomeSceneController {
    // the main pane of this scene, control the change of scene
    @FXML
    private AnchorPane mainPane;

    @FXML
    private Button dashBoard;

    @FXML
    private Button inFlightSystem;

    @FXML
    private Button survey;

    @FXML
    private VBox vBoxLeft;

    //event when the client click TripDashboard button, change the scene to TripDashBoard
    @FXML
    void startDashBoard(ActionEvent event) {
        try {
            mainPane.getScene().setRoot(FXMLLoader.load(ClientApplication.class.getResource(ClientApplication.SINGLE_TRIPDASHBOARD_PATH)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //event when the client click InFlightSystem button, change the scene to InFlightSystem
    @FXML
    void startInFlightSystem(ActionEvent event) {
        try {
            mainPane.getScene().setRoot(FXMLLoader.load(ClientApplication.class.getResource(ClientApplication.IN_FLIGHT_PATH)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //event when the client click Survey button, change the scene to Survey
    @FXML
    void startSurvey(ActionEvent event) {
        try {
            mainPane.getScene().setRoot(FXMLLoader.load(ClientApplication.class.getResource(ClientApplication.SURVEY_PATH)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**=========================================================request=========================================== */


    public void initialize() {

    }


}
