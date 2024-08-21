package de.tum.nihaoshijie.flightsystem.client.controller;import com.jfoenix.controls.JFXButton;import de.tum.nihaoshijie.flightsystem.server.persistence.Entertainment;import de.tum.nihaoshijie.flightsystem.server.persistence.InFlightService;import de.tum.nihaoshijie.flightsystem.server.persistence.UserInflightservice;import de.tum.nihaoshijie.flightsystem.server.presentation.UserController;import javafx.event.ActionEvent;import javafx.fxml.FXML;import javafx.fxml.FXMLLoader;import javafx.scene.control.ComboBox;import javafx.scene.control.Label;import javafx.scene.layout.AnchorPane;import javafx.scene.media.MediaPlayer;import org.springframework.core.ParameterizedTypeReference;import org.springframework.http.HttpHeaders;import org.springframework.http.MediaType;import org.springframework.web.reactive.function.client.WebClient;import java.io.IOException;import java.time.Instant;import java.util.*;public class ServiceController {    private static final String SERVICE_MAIN_PATH = "/clientview/fxmls/ServiceMain.fxml";    private WebClient webClient;    private List<InFlightService> allServices;    @FXML    private JFXButton backButton;    @FXML    private JFXButton cancelButton;    @FXML    private JFXButton confirmButton;    @FXML    private AnchorPane mainPane;    @FXML    private ComboBox<String> serviceNameCB;    @FXML    private ComboBox<String> typeCB;    @FXML    private Label messageField;    @FXML    void initialize() {        webClient = WebClient.builder().baseUrl("http://localhost:8080/")                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)                .build();        allServices = webClient.get().uri(urlBuilder -> urlBuilder                        .path("/inFlightService/all")                        .build())                .retrieve()                .bodyToMono(new ParameterizedTypeReference<List<InFlightService>>() {                })                .onErrorStop()                .block();        if (allServices == null || allServices.size() == 0) {            return;        }        Set<String> allTypes = new HashSet<>();        allServices.forEach(s -> allTypes.add(s.getType()));        typeCB.getItems().setAll(allTypes.stream().sorted().toList());    }    @FXML    void back(ActionEvent event) throws IOException {        mainPane.getScene().setRoot(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(SERVICE_MAIN_PATH))));    }    @FXML    void cancel(ActionEvent event) {        // TODO: what should cancel do?    }    @FXML    void confirm(ActionEvent event) {        InFlightService requiredService = null;        for (var s : allServices) {            if (s.getType().equals(typeCB.getValue()) && s.getDescription().equals(serviceNameCB.getValue())) {                requiredService = s;                break;            }        }        Map<String, Object> record = new HashMap<>();        record.put("user", UserInfo.getUser());        record.put("service", requiredService);        InFlightService finalRequiredService = requiredService;        final UserInflightservice userInflightservice = webClient.post()                .uri(uriBuilder -> uriBuilder                        .path("/inFlightService/user-inflightservice")                        .queryParam("userId", UserInfo.getUser().getId())                        .queryParam("serviceId", finalRequiredService.getId())                        .build())                .retrieve()                .bodyToMono(new ParameterizedTypeReference<UserInflightservice>() {                })                .onErrorStop()                .block();        if (userInflightservice != null) {            messageField.setText("Dear " + userInflightservice.getUser().getUserName() + ", your required service for " + userInflightservice.getInflightservice().getDescription() + " has been sent successfully!");        }    }    @FXML    void selectServiceName(ActionEvent event) {        typeCB.getSelectionModel().select(typeCB.getValue());    }    @FXML    void selectType(ActionEvent event) {        typeCB.getSelectionModel().select(typeCB.getValue());        serviceNameCB.getItems().clear();        allServices.stream().filter(s -> s.getType().equals(typeCB.getValue())).forEach(s -> serviceNameCB.getItems().add(s.getDescription()));    }}