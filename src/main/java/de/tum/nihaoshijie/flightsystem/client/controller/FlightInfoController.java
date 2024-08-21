package de.tum.nihaoshijie.flightsystem.client.controller;

import com.jfoenix.controls.JFXButton;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.view.javafx.BrowserView;
import de.tum.nihaoshijie.flightsystem.client.ClientApplication;
import de.tum.nihaoshijie.flightsystem.server.persistence.City;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FlightInfoController {
    private static final String YOUR_JXBROWSER_API_KEY = "1BNDHFSC1G37PSWMFSX55VVN4XU6C9CPOAGP6FRS92YG4A9QVGG1UYPB9105OFTY0W9ETE";
    private static final String POI_SCENE_PATH = "/clientview/fxmls/PoiScene.fxml";
    private Browser browser;

    @FXML
    private Label airlineTextField;

    @FXML
    private Label airplaneTextField;

    @FXML
    private Label arrCityTextField;

    @FXML
    private Label arrGateTextField;

    @FXML
    private Label arrTeriminalTextField;

    @FXML
    private Label arrTimeTextField;

    @FXML
    private Label depCityTextField;

    @FXML
    private Label depGateTextField;

    @FXML
    private Label depTeriminalTextField;

    @FXML
    private Label depTimeTextField;

    @FXML
    private JFXButton flightLineButton;

    @FXML
    private JFXButton arrCityButton;

    @FXML
    private JFXButton depCityButton;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private BorderPane mapViewPane;

    @FXML
    void goBack(ActionEvent event) {
        try {
            // change the scene to trip dashboard
            mainPane.getScene().setRoot(FXMLLoader.load(getClass().getResource(ClientApplication.SINGLE_TRIPDASHBOARD_PATH)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void showArrCityInfo(ActionEvent event) {
        changeToPoiScene(UserInfo.selectedFlight.getArrCityObj().getName(), UserInfo.selectedFlight.getFlightDate().get());
    }

    @FXML
    void showDepCityInfo(ActionEvent event) {
        changeToPoiScene(UserInfo.selectedFlight.getDepCityObj().getName(), UserInfo.selectedFlight.getFlightDate().get());
    }

    @FXML
    void showFlightLine(ActionEvent event) {
        final City arr = UserInfo.selectedFlight.getArrCityObj();
        final City dep = UserInfo.selectedFlight.getDepCityObj();
        if (UserInfo.selectedFlightMutiStop == null) {
            browser.mainFrame().ifPresent(frame -> frame.executeJavaScript(buildFlightLineScript(dep.getLat(), arr.getLat(), dep.getLon(), arr.getLon())));
        } else {
            browser.mainFrame().ifPresent(frame -> frame.executeJavaScript(buildFlightLineScript(dep.getLat(), arr.getLat(), dep.getLon(), arr.getLon())));
            browser.mainFrame().ifPresent(frame -> frame.executeJavaScript(buildFlightLineScript(arr.getLat(), dep.getLat(), arr.getLon(), dep.getLon())));
        }
        final List<City> cities = Arrays.asList(UserInfo.selectedFlight.getArrCityObj(), UserInfo.selectedFlight.getDepCityObj());
        setMarker(cities);
    }

    @FXML
    public void initialize() {
        // set all the textfield with right content
        FlightProperty selectedFlight = UserInfo.selectedFlight;
        airlineTextField.setText(selectedFlight.getAirline().get());
        airplaneTextField.setText(selectedFlight.getAirplane().get());
        arrCityButton.setText(selectedFlight.getArrCity().get());
        arrCityTextField.setText(selectedFlight.getArrCity().get() + "(" + selectedFlight.getArrAirport().get() + ")");
        arrGateTextField.setText("Gate:" + selectedFlight.getArrGate().get());
        arrTeriminalTextField.setText("T:" + selectedFlight.getArrTerminal().get());
        arrTimeTextField.setText(selectedFlight.getArrTimeEst().get());
        depCityButton.setText(selectedFlight.getDepCity().get());
        depCityTextField.setText(selectedFlight.getDepCity().get() + "(" + selectedFlight.getDepAirport().get() + ")");
        depGateTextField.setText("Gate:" + selectedFlight.getDepGate().get());
        depTeriminalTextField.setText("T:" + selectedFlight.getDepTerminal().get());
        depTimeTextField.setText(selectedFlight.getDepTimeEst().get());

        //intialize the mapview on the mapViewPane
        browser = Engine.newInstance(EngineOptions.newBuilder(RenderingMode.OFF_SCREEN)
                        .licenseKey(YOUR_JXBROWSER_API_KEY).build())
                .newBrowser();
        mapViewPane.setCenter(BrowserView.newInstance(browser));
        browser.navigation().loadUrl(Objects.requireNonNull(this.getClass().getResource("/clientview/html/flightmap.html")).toString());
    }

    private String buildFlightLineScript(double lat1, double lat2, double lng1, double lng2) {
        return String.format("var Lat = [%s, %s];\nvar Lng = [%s, %s];\ndrawFlightLine(Lat, Lng);", lat1, lat2, lng1, lng2);
    }

    private String buildMarkerScript(double lat, double lng, String info) {
        return String.format("addMarker('%s', '%s', '%s');", lat, lng, info);
    }

    // TODO: add lat and lon to entity city and then implement this method!
    private void setMarker(List<City> cities) {
        cities.forEach(p -> browser.mainFrame().ifPresent(frame -> frame.executeJavaScript(buildMarkerScript(p.getLat(), p.getLon(), p.getName()))));
    }

    private void changeToPoiScene(String cityName, String date) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(POI_SCENE_PATH));
        Parent rootNode = null;
        try {
            rootNode = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        mainPane.getScene().setRoot(rootNode);
        PoiController poiController = fxmlLoader.getController();
        poiController.setCityName(cityName);
        poiController.setDate(LocalDate.parse(date));
        poiController.setupMapView();
    }
}
