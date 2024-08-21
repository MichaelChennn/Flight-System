package de.tum.nihaoshijie.flightsystem.client.controller;

import com.jfoenix.controls.JFXButton;
import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.view.javafx.BrowserView;
import de.tum.nihaoshijie.flightsystem.server.persistence.Poi;
import de.tum.nihaoshijie.flightsystem.server.persistence.Weather;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PoiController {
    private static final String FLIGHTINFO_PATH = "/clientview/fxmls/FlightInfo.fxml";
    private static final String YOUR_JXBROWSER_API_KEY = "1BNDHFSC1G37PSWMFSX55VVN4XU6C9CPOAGP6FRS92YG4A9QVGG1UYPB9105OFTY0W9ETE";
    private static final String FAVOURITE_ICON_PATH = "/clientview/resource/poiSceneIcons/favorite_Icon.png";
    private static final String FAVOURITE_ADDED_ICON_PATH = "/clientview/resource/poiSceneIcons/favorite_added_Icon.png";
    List<Poi> pois = new ArrayList<>();
    Weather weather;
    private String cityName;
    private LocalDate date;
    private Browser browser;
    private WebClient webClient;

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @FXML
    private JFXButton allButton;
    @FXML
    private JFXButton backButton;
    @FXML
    private Label cityNameLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private JFXButton hotelButton;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private BorderPane mapViewPane;
    @FXML
    private JFXButton otherButton;
    @FXML
    private JFXButton restaurantButton;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton sightseeingButton;
    @FXML
    private Label temperatureInfo;
    @FXML
    private Label typeLabel;
    @FXML
    private Label weatherInfo;
    @FXML
    private ImageView weatherImage;
    @FXML
    private ComboBox<String> selectedPoiCB;
    @FXML
    private JFXButton likeButton;

    @FXML
    void back(ActionEvent event) {
        try {
            mainPane.getScene().setRoot(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(FLIGHTINFO_PATH))));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void selectPoi(ActionEvent event) {
        selectedPoiCB.getSelectionModel().select(selectedPoiCB.getValue());
    }

    @FXML
    void save(ActionEvent event) {
        long[] poiId = pois.stream().filter(p -> p.getName().equals(selectedPoiCB.getValue())).mapToLong(Poi::getId).toArray();
        webClient.post().uri(uriBuilder -> uriBuilder
                        .path("/pois/user-list")
                        .queryParam("userId", UserInfo.getUser().getId())
                        .build())
                .bodyValue(poiId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Poi>>() {
                })
                .onErrorStop()
                .block();
    }

    @FXML
    void showLikes(ActionEvent event) {
        final List<Poi> userPois = webClient.get().uri(uriBuilder -> uriBuilder
                        .path("/pois/" + UserInfo.user.getId() + "/user-list")
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Poi>>() {
                })
                .onErrorStop()
                .block();
        if(userPois==null || userPois.isEmpty()) {
            return;
        }
        browser.mainFrame().ifPresent(frame -> frame.executeJavaScript("deleteMarkers()"));
        setMarker(userPois);
    }

    @FXML
    void showAll(ActionEvent event) {
        setMarker(pois);
        pois.forEach(p -> {
            selectedPoiCB.getItems().add(p.getName());
        });
    }

    @FXML
    void showHotel(ActionEvent event) {
        final List<Poi> hotels = showSelectedType("HOTEL");
        if (hotels == null || hotels.isEmpty()) {
            return;
        }
        hotels.forEach(p -> {
            selectedPoiCB.getItems().add(p.getName());
        });
    }

    @FXML
    void showOther(ActionEvent event) {
        final List<Poi> others = showSelectedType("OTHER");
        if (others == null || others.isEmpty()) {
            return;
        }
        others.forEach(p -> {
            selectedPoiCB.getItems().add(p.getName());
        });
    }

    @FXML
    void showRestaurant(ActionEvent event) {
        final List<Poi> restaurants = showSelectedType("RESTAURANT");
        if (restaurants == null || restaurants.isEmpty()) {
            return;
        }
        restaurants.forEach(p -> {
            selectedPoiCB.getItems().add(p.getName());
        });
    }

    @FXML
    void showSightSeeing(ActionEvent event) {
        final List<Poi> sightseeings = showSelectedType("SIGHTSEEING");
        if (sightseeings == null || sightseeings.isEmpty()) {
            return;
        }
        sightseeings.forEach(p -> {
            selectedPoiCB.getItems().add(p.getName());
        });
    }

    @FXML
    public void initialize() {
        webClient = WebClient.builder().baseUrl("http://localhost:8080/")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        browser = Engine.newInstance(EngineOptions.newBuilder(RenderingMode.OFF_SCREEN)
                        .licenseKey(YOUR_JXBROWSER_API_KEY).build())
                .newBrowser();
        mapViewPane.setCenter(BrowserView.newInstance(browser));
    }

    public void setupMapView() {
        browser.navigation().loadUrl(Objects.requireNonNull(this.getClass().getResource("/clientview/html/poimap.html")).toString());
        // get pois
        pois.addAll(Objects.requireNonNull(webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("pois")
                        .queryParam("city", cityName)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Poi>>() {
                })
                .onErrorStop()
                .block()));

        // get weather
        weather = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("weather")
                        .queryParam("city", cityName) // cityName.getText()
                        .queryParam("date", date)
                        .build())
                .retrieve()
                .bodyToMono(Weather.class)
                .onErrorStop()
                .block();
        setMarker(pois);
        showCityInfo();
    }

    private void showCityInfo() {
        cityNameLabel.setText(cityName);
        temperatureInfo.setText(weather.getHighTemp().toString() + (char) 176 + "C");
        weatherInfo.setText(weather.getWeatherCondition());
        dateLabel.setText(date.toString());
    }

    private void setMarker(List<Poi> pois) {
        pois.forEach(p -> browser.mainFrame().ifPresent(frame -> frame.executeJavaScript(buildMarkerScript(p.getLat(), p.getLon(), poiInfo(p)))));
    }


    private String buildMarkerScript(double lat, double lng, String info) {
        return String.format("addMarker('%s', '%s', '%s');", lat, lng, info);
    }

    private String poiInfo(Poi poi) {
        return String.format("Name: %s; Type: %s; Address: %s; Rate: %s; Website: %s", poi.getName(), poi.getType().toLowerCase(), poi.getAddress(), poi.getRate(), poi.getUrl());
    }

    private List<Poi> showSelectedType(String type) {
        try {
            final List<Poi> selected = pois.stream().filter(p -> type.equals(p.getType())).collect(Collectors.toList());
            browser.mainFrame().ifPresent(frame -> frame.executeJavaScript("deleteMarkers()"));
            setMarker(selected);
            return selected;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
