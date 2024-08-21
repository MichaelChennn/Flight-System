package de.tum.nihaoshijie.flightsystem.client;

import java.io.IOException;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class ClientApplication extends Application {
    // all ViewController in the whole flight system
    public static final String SINGLE_TRIPDASHBOARD_PATH = "/clientview/fxmls/DashBoardSingleStop.fxml";
    public static final String LOGIN_PATH = "/clientview/fxmls/Login.fxml";
    public static final String REGISTER_PATH = "/clientview/fxmls/Register.fxml";
    public static final String HOMESCENE_PATH = "/clientview/fxmls/HomeScene.fxml";
    public static final String MUTI_TRIPDASHBOARD_PATH = "/clientview/fxmls/DashBoardMutiStop.fxml";
    public static final String FLIGHT_INFO_PATH = "/clientview/fxmls/FlightInfo.fxml";
    public static final String POI_SCENE_PATH = "/clientview/fxmls/PoiScene.fxml";
    public static final String IN_FLIGHT_PATH = "/clientview/fxmls/ServiceMain.fxml";
    public static final String SURVEY_PATH = "/clientview/fxmls/Survey.fxml";

    private boolean useLoginScene = true;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // set stage unresizeable
        primaryStage.setResizable(false);

        // settitle
        System.out.println("Flight System start!");
        primaryStage.setTitle("Flight System - NHSJ");

        if (!useLoginScene) {
            setStageWithoutLoginScene(primaryStage);
        } else {
            setStageWithLoginScene(primaryStage);
        }

    }

    public static void setStageWithoutLoginScene(Stage primaryStage) throws IOException {
        primaryStage.initStyle(StageStyle.DECORATED);
        // load homescene fxml file
        Parent homesceneroot = FXMLLoader.load(ClientApplication.class.getResource(ClientApplication.HOMESCENE_PATH));

        // set Icon with file path
        Image image = new Image(ClientApplication.class.getResourceAsStream("/clientview/resource/Icon.png"));
        primaryStage.getIcons().add(image);

        // set homeScene with fxml file
        primaryStage.setScene(new Scene(homesceneroot));
        primaryStage.show();

    }

    public void setStageWithLoginScene(Stage primaryStage) throws IOException {
        // set Icon with file path
        Image image = new Image(getClass().getResourceAsStream("/clientview/resource/Icon.png"));
        primaryStage.getIcons().add(image);

        // load login fxml file
        Parent loginSceneroot = FXMLLoader.load(getClass().getResource(ClientApplication.LOGIN_PATH));
        Scene scene = new Scene(loginSceneroot);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    /**
     * generate and display warining dialog
     * @param title of the warining 
     * @param body content of the warning
     * @param stage on the pane where you want display the warining
     */
    public static void displayWarining(String title, String body, Window stage) {
        JFXAlert alert = new JFXAlert(stage);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(title));
        content.setBody(new Text(body));
        JFXButton button = new JFXButton("OK");
        //button.getStyleClass().add("dialog-accept");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                alert.close();;
            };
        });
        content.setActions(button);
        alert.setContent(content);
        alert.show();
    }

}
