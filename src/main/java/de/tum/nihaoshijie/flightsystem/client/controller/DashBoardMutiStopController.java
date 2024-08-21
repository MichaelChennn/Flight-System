package de.tum.nihaoshijie.flightsystem.client.controller;

import com.jfoenix.controls.*;

import de.tum.nihaoshijie.flightsystem.client.ClientApplication;
import de.tum.nihaoshijie.flightsystem.server.persistence.Flight;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javafx.scene.text.Font;
import javafx.stage.Window;
import javafx.util.Callback;

//import org.jboss.jandex.Main;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

public class DashBoardMutiStopController {
    /**
     * Button go back
     */
    @FXML
    private JFXButton backButton;
    @FXML
    private JFXButton singleStopButton;
    @FXML
    private JFXButton firstSearchButton;
    @FXML
    private JFXButton secondSearchButton;
    @FXML
    private JFXButton mapButton;
    @FXML
    private JFXButton likeButton;

    @FXML
    private DatePicker firstDatePicker;

    @FXML
    private JFXComboBox<String> thirdCbox;
    @FXML
    private JFXComboBox<String> secondCbox;
    @FXML
    private JFXComboBox<String> firstCbox;
    @FXML
    private JFXComboBox<String> userFlightListCbox;

    @FXML
    private JFXTreeTableView<FlightProperty> flightsTable;

    @FXML
    private Tooltip firstToolTip;
    @FXML
    private Tooltip secondToolTip;
    @FXML
    private Tooltip thirdToolTip;
    @FXML
    private Tooltip userFlightTooltip;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Label firstIcao;
    @FXML
    private Label secondIcao;
    @FXML
    private Label firstAirport;
    @FXML
    private Label secondAirport;
    @FXML
    private Label thirdAirport;
    @FXML
    private Label fourthAirport;
    /**
     * Create a webclient
     */
    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    private String firstCity;
    private String secondCity;
    private String thirdCity;

    private ObservableList<String> firstCityList;
    private ObservableList<String> secondCityList;
    private ObservableList<String> thirdCityList;
    private ObservableList<String> userFlightList;

    private LocalDate firstDate;

    private String firstCityFilter = "";
    private String secondCityFilter = "";
    private String thirdCityFilter = "";
    private String userFlightListFilter = "";

    private FlightProperty rootProperty = new FlightProperty("flightDate", "depCity", "arrCity", "depAirport",
            "depTerminal", "depGate", "depTimeEst", "arrAirport", "arrTerminal", "arrGate",
            "arrTimeEst", "airline", "airplane", "FlightCode", null, null, null);
    private TreeItem<FlightProperty> root = new TreeItem<>(rootProperty);
    private TripModel tripModel = null;

    // ========================================Initialization=================================================
    public void initialize() throws IOException {
        // 1.initialize the user flight list
        initializeUserFlightList();
        // 2.initialize items in comboboxs
        initializeListOfbox(firstCbox, secondCbox, thirdCbox);
        // 3.initialize eventhandler of comboboxs
        initializeCombobox(firstCbox, secondCbox, thirdCbox);
        // 4.initialize the treetableview
        initializeTreeTableView();
    }

    /**
     * initialize the user flight list form server site
     */
    private void initializeUserFlightList() {
        getUserFlightList();
    }

    /**
     * get a flight list from server with userId
     */
    private void getUserFlightList() {
        // when user not login, then is the id -99
        if (UserInfo.user == null) {
            userFlightList = FXCollections.observableArrayList();
            return;
        }
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("flight/user-list")
                        .queryParam("userId", UserInfo.user.getId())
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Flight>>() {
                })
                .onErrorStop()
                .subscribe(this::setUserFlightList);
    }

    /**
     * Save the user flight in UserInfo class and set the combobox with correct list
     *
     * @param updatedList the new userflightlist from server site
     */
    private void setUserFlightList(List<Flight> updatedList) {
        Platform.runLater(() -> {
            UserInfo.updateUserFlightList(updatedList);
            userFlightList = UserInfo.convertUserFlightListInString();
            userFlightListCbox.getItems().setAll(userFlightList);
        });
    }

    private void initializeListOfbox(JFXComboBox<String> firstCbox, JFXComboBox<String> secondCbox,
                                     JFXComboBox<String> thirdCBox) {
        // add items in the list
        firstCbox.getItems().addAll("NKG Nanjing, CN", "PVG Shanghai, CN", "SHA Shanghai, CN", "MUC Munich, DE", "LIS Lisbon, PT");
        secondCbox.getItems().addAll("NKG Nanjing, CN", "PVG Shanghai, CN", "SHA Shanghai, CN", "MUC Munich, DE", "LIS Lisbon, PT");
        thirdCbox.getItems().addAll("NKG Nanjing, CN", "PVG Shanghai, CN", "SHA Shanghai, CN", "MUC Munich, DE", "LIS Lisbon, PT");
        // save the list
        firstCityList = FXCollections.observableArrayList(firstCbox.getItems());
        secondCityList = FXCollections.observableArrayList(secondCbox.getItems());
        thirdCityList = FXCollections.observableArrayList(thirdCbox.getItems());

    }

    private void initializeCombobox(JFXComboBox<String> firstCbox, JFXComboBox<String> secondCbox,
                                    JFXComboBox<String> thirdCBox) {

        firstCbox.setTooltip(firstToolTip);
        secondCbox.setTooltip(secondToolTip);
        thirdCbox.setTooltip(thirdToolTip);
    }

    private void initializeTreeTableView() {
        /**
         * Treetableview layout
         */
        flightsTable.setColumnResizePolicy(JFXTreeTableView.CONSTRAINED_RESIZE_POLICY);
        /**
         * initialize the column of the flightsTreetableview, use cellvalueFactory to
         * display the correct content.
         */
        JFXTreeTableColumn<FlightProperty, String> dateColum = new JFXTreeTableColumn<>("Date");
        dateColum.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<FlightProperty, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<FlightProperty, String> param) {
                        return param.getValue().getValue().getFlightDate();
                    }
                });
        /**
         * Flight Airline colum
         */
        JFXTreeTableColumn<FlightProperty, String> airLineColum = new JFXTreeTableColumn<>("Airline");
        airLineColum.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<FlightProperty, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<FlightProperty, String> param) {
                        return param.getValue().getValue().getAirline();
                    }
                });
        /**
         * Departure Airport Column
         */
        JFXTreeTableColumn<FlightProperty, String> airPortColumn = new JFXTreeTableColumn<>("Airport");
        airPortColumn.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<FlightProperty, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<FlightProperty, String> param) {

                        return param.getValue().getValue().getDepAirport();
                    }
                });
        /**
         * Departure time Column
         */
        JFXTreeTableColumn<FlightProperty, String> dep_Time_Column = new JFXTreeTableColumn<>("Time");
        dep_Time_Column.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<FlightProperty, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<FlightProperty, String> param) {

                        return param.getValue().getValue().getDepTimeEst();
                    }
                });

        /**
         * Departure City Column
         */
        JFXTreeTableColumn<FlightProperty, String> dep_City_Column = new JFXTreeTableColumn<>("Departure");
        dep_City_Column.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<FlightProperty, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<FlightProperty, String> param) {

                        return param.getValue().getValue().getDepCity();
                    }
                });

        /**
         * Destination Airport Column
         */
        JFXTreeTableColumn<FlightProperty, String> arr_Airport_Column = new JFXTreeTableColumn<>("Airport");
        arr_Airport_Column.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<FlightProperty, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<FlightProperty, String> param) {

                        return param.getValue().getValue().getArrAirport();
                    }
                });
        /**
         * Destination Time Column
         */
        JFXTreeTableColumn<FlightProperty, String> arr_Time_Column = new JFXTreeTableColumn<>("Time");
        arr_Time_Column.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<FlightProperty, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<FlightProperty, String> param) {

                        return param.getValue().getValue().getArrTimeEst();
                    }
                });
        /**
         * Destination City Column
         */
        JFXTreeTableColumn<FlightProperty, String> arr_City_Column = new JFXTreeTableColumn<>("Destination");
        arr_City_Column.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<FlightProperty, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<FlightProperty, String> param) {
                        return param.getValue().getValue().getArrCity();
                    }
                });

        /**
         * Icao Column
         */
        JFXTreeTableColumn<FlightProperty, String> iCao_Column = new JFXTreeTableColumn<>("FlightCode");
        iCao_Column.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<FlightProperty, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<FlightProperty, String> param) {
                        return param.getValue().getValue().getiCao();
                    }
                });

        flightsTable.getColumns().add(dateColum);
        flightsTable.getColumns().add(airLineColum);
        flightsTable.getColumns().add(airPortColumn);
        flightsTable.getColumns().add(dep_Time_Column);
        flightsTable.getColumns().add(dep_City_Column);
        flightsTable.getColumns().add(arr_Airport_Column);
        flightsTable.getColumns().add(arr_Time_Column);
        flightsTable.getColumns().add(arr_City_Column);
        flightsTable.getColumns().add(iCao_Column);
        flightsTable.setRoot(root);
        flightsTable.setShowRoot(false);

    }

    // ========================================Event Handler=================================================
    @FXML
    void handleFirstDatepicker(ActionEvent event) {
        if (firstDatePicker.getValue() != null) {
            this.firstDate = firstDatePicker.getValue();
        }
    }

    @FXML
    void selectFlight(MouseEvent event) {
        ObservableList<TreeItem<FlightProperty>> selectedItems = flightsTable.getSelectionModel().getSelectedItems();
        if (selectedItems.size() == 0) {
            mapButton.setDisable(true);
            likeButton.setDisable(true);
            return;
        } else {
            mapButton.setDisable(false);
            likeButton.setDisable(false);
            if (tripModel == TripModel.First) {
                UserInfo.selectedFlight = selectedItems.get(0).getValue();
                FlightProperty f = UserInfo.selectedFlight;
                firstIcao.setText(f.getiCao().get());
                firstAirport.setText(f.getDepAirport().get());
                secondAirport.setText(f.getArrAirport().get());
            } else if (tripModel == TripModel.Second) {
                UserInfo.selectedFlightMutiStop = selectedItems.get(0).getValue();
                FlightProperty f = UserInfo.selectedFlightMutiStop;
                secondIcao.setText(f.getiCao().get());
                thirdAirport.setText(f.getDepAirport().get());
                fourthAirport.setText(f.getArrAirport().get());
            }
        }
    }
    // ========================================Scene change=================================================

    /**
     * change to single stop Scene
     *
     * @param event mouse click on singleStopButton
     */
    @FXML
    void changeToSingleStopScene(ActionEvent event) {
        try {
            // remove the saved flight
            UserInfo.selectedFlight = null;
            UserInfo.selectedFlightMutiStop = null;
            // change the scene to homescene
            mainPane.getScene()
                    .setRoot(FXMLLoader.load(getClass().getResource(ClientApplication.SINGLE_TRIPDASHBOARD_PATH)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * change to home scene
     *
     * @param event click on back Button
     */
    @FXML
    void goBack(ActionEvent event) {
        try {
            // remove the saved flight
            UserInfo.selectedFlight = null;
            UserInfo.selectedFlightMutiStop = null;
            // change the scene to homescene
            mainPane.getScene().setRoot(FXMLLoader.load(getClass().getResource(ClientApplication.HOMESCENE_PATH)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ========================================Search part=================================================
    @FXML
    void firstButtonSearch(ActionEvent event) {
        if (this.firstCity == null) {
            ClientApplication.displayWarining("First Departure not found",
                    "Please choose the departure of your trip!", mainPane.getScene().getWindow());
            return;
        }
        if (this.secondCity == null) {
            ClientApplication.displayWarining("First Destination not found",
                    "Please choose the destination of your trip!", mainPane.getScene().getWindow());
            return;
        }
        if (this.firstDate == null) {
            ClientApplication.displayWarining("Date not found",
                    "Please choose the date of your trip!", mainPane.getScene().getWindow());
            return;
        }
        if (root != null && root.getChildren().size() != 0) {
            root.getChildren().clear();
        }
        tripModel = TripModel.First;
        getTripFlight(this.firstCity, this.secondCity);
    }

    @FXML
    void secondButtonSearch(ActionEvent event) {
        if (this.secondCity == null) {
            ClientApplication.displayWarining("First Departure not found",
                    "Please choose the departure of your trip!", mainPane.getScene().getWindow());
            return;
        }
        if (this.thirdCity == null) {
            ClientApplication.displayWarining("First Destination not found",
                    "Please choose the destination of your trip!", mainPane.getScene().getWindow());
            return;
        }

        if (this.firstDate == null) {
            ClientApplication.displayWarining("Date not found",
                    "Please choose the date of your trip!", mainPane.getScene().getWindow());
            return;
        }
        if (root != null && root.getChildren().size() != 0) {
            root.getChildren().clear();
        }
        tripModel = TripModel.Second;
        getTripFlight(this.secondCity, this.thirdCity);
    }

    private void getTripFlight(String firstCity, String secondCity) {
        List<Flight> flights =
                this.webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("flight")
                                .queryParam("origin", firstCity)
                                .queryParam("destination", secondCity)
                                .queryParam("date", firstDate.toString())
                                .build())
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<Flight>>() {
                        })
                        .block();
        setFlights(flights);
    }

    private void setFlights(List<Flight> flights) {
        if (flights.size() == 0) {
            ClientApplication.displayWarining("Flights not found",
                    "We haven't found any flights on this date. Please choose another date", mainPane.getScene().getWindow());
            return;
        }
        Platform.runLater(() -> {
            for (Flight flight : flights) {
                root.getChildren().add(new TreeItem<FlightProperty>(new FlightProperty(flight.getFlightDate().toString(),
                        flight.getDepCity().toString(),
                        flight.getArrCity().toString(),
                        flight.getDepAirport().getIata(),
                        "T" + flight.getDepTerminal().toString(),
                        flight.getDepGate(),
                        flight.getDepTimeEst().toString().substring(12, 16),
                        flight.getArrAirport().getIata(),
                        "T" + flight.getArrTerminal().toString(),
                        flight.getArrGate(),
                        flight.getArrTimeEst().toString().substring(12, 16),
                        flight.getAirline(),
                        flight.getAirplane(),
                        flight.getIcao(),
                        flight.getId(),
                        flight.getDepCity(),
                        flight.getArrCity())));
            }
        });
    }

    // ========================================Combobox=================================================
    @FXML
    void keyPressedFirstCbox(KeyEvent event) {
        ObservableList<String> filteredList = FXCollections.observableArrayList();
        KeyCode code = event.getCode();
        // get user input and save it in filter
        if (code.isLetterKey()) {
            firstCityFilter += event.getText();
        }
        // edit the tooltips when user press back space
        if (code == KeyCode.BACK_SPACE && firstCityFilter.length() > 0) {
            firstCityFilter = firstCityFilter.substring(0, firstCityFilter.length() - 1);
            firstCbox.getItems().setAll(firstCityList);
        }

        if (code == KeyCode.ESCAPE) {
            firstCityFilter = "";
        }
        // hide the tooltips when there is no user input
        if (firstCityFilter.length() == 0) {
            filteredList = firstCityList;
            firstToolTip.hide();
        } else {
            // show the correct content with tooltip;
            Stream<String> itens = firstCbox.getItems().stream();
            String txtUsr = firstCityFilter.toString().toLowerCase();
            // sort the items in combobox
            itens.filter(el -> el.toString().toLowerCase().contains(txtUsr)).forEach(filteredList::add);
            firstToolTip.setText(txtUsr);
            Window stage = firstCbox.getScene().getWindow();

            double posX = stage.getX() + firstCbox.getBoundsInParent().getMinX();
            double posY = stage.getY() + firstCbox.getBoundsInParent().getMinY();

            firstToolTip.setFont(new Font("Arial", 11));
            firstToolTip.show(stage, posX, posY);
            firstCbox.show();

        }
        firstCbox.getItems().setAll(filteredList);
    }

    @FXML
    void hideFirst(Event event) {
        firstCityFilter = "";
        firstToolTip.hide();
        String s = firstCbox.getSelectionModel().getSelectedItem();
        firstCbox.getItems().setAll(firstCityList);
        firstCbox.getSelectionModel().select(s);
    }

    @FXML
    void keyPressedSecondCbox(KeyEvent event) {
        ObservableList<String> filteredList = FXCollections.observableArrayList();
        KeyCode code = event.getCode();
        // get user input and save it in filter
        if (code.isLetterKey()) {
            secondCityFilter += event.getText();
        }
        // edit the tooltips when user press back space
        if (code == KeyCode.BACK_SPACE && secondCityFilter.length() > 0) {
            secondCityFilter = secondCityFilter.substring(0, secondCityFilter.length() - 1);
            secondCbox.getItems().setAll(secondCityList);
        }

        if (code == KeyCode.ESCAPE) {
            secondCityFilter = "";
        }
        // hide the tooltips when there is no user input
        if (secondCityFilter.length() == 0) {
            filteredList = secondCityList;
            secondToolTip.hide();
        } else {
            // show the correct content with tooltip;
            Stream<String> itens = secondCbox.getItems().stream();
            String txtUsr = secondCityFilter.toString().toLowerCase();
            // sort the items in combobox
            itens.filter(el -> el.toString().toLowerCase().contains(txtUsr)).forEach(filteredList::add);
            secondToolTip.setText(txtUsr);
            Window stage = secondCbox.getScene().getWindow();

            double posX = stage.getX() + secondCbox.getBoundsInParent().getMinX();
            double posY = stage.getY() + secondCbox.getBoundsInParent().getMinY();

            secondToolTip.setFont(new Font("Arial", 11));
            secondToolTip.show(stage, posX, posY);
            secondCbox.show();

        }
        secondCbox.getItems().setAll(filteredList);
    }

    @FXML
    void hideSecond(Event event) {
        secondCityFilter = "";
        secondToolTip.hide();
        String s = secondCbox.getSelectionModel().getSelectedItem();
        secondCbox.getItems().setAll(secondCityList);
        secondCbox.getSelectionModel().select(s);
    }

    @FXML
    void keyPressedThirdCbox(KeyEvent event) {
        ObservableList<String> filteredList = FXCollections.observableArrayList();
        KeyCode code = event.getCode();
        // get user input and save it in filter
        if (code.isLetterKey()) {
            thirdCityFilter += event.getText();
        }
        // edit the tooltips when user press back space
        if (code == KeyCode.BACK_SPACE && thirdCityFilter.length() > 0) {
            thirdCityFilter = thirdCityFilter.substring(0, thirdCityFilter.length() - 1);
            thirdCbox.getItems().setAll(thirdCityList);
        }

        if (code == KeyCode.ESCAPE) {
            thirdCityFilter = "";
        }
        // hide the tooltips when there is no user input
        if (thirdCityFilter.length() == 0) {
            filteredList = thirdCityList;
            thirdToolTip.hide();
        } else {
            // show the correct content with tooltip;
            Stream<String> itens = thirdCbox.getItems().stream();
            String txtUsr = thirdCityFilter.toString().toLowerCase();
            // sort the items in combobox
            itens.filter(el -> el.toString().toLowerCase().contains(txtUsr)).forEach(filteredList::add);
            thirdToolTip.setText(txtUsr);
            Window stage = thirdCbox.getScene().getWindow();

            double posX = stage.getX() + thirdCbox.getBoundsInParent().getMinX();
            double posY = stage.getY() + thirdCbox.getBoundsInParent().getMinY();

            thirdToolTip.setFont(new Font("Arial", 11));
            thirdToolTip.show(stage, posX, posY);
            thirdCbox.show();

        }
        thirdCbox.getItems().setAll(filteredList);
    }

    @FXML
    void hideThird(Event event) {
        thirdCityFilter = "";
        thirdToolTip.hide();
        String s = thirdCbox.getSelectionModel().getSelectedItem();
        thirdCbox.getItems().setAll(thirdCityList);
        thirdCbox.getSelectionModel().select(s);
    }

    @FXML
    void handleUserListCbox(KeyEvent event) {

    }

    @FXML
    void hideUserFlightList(Event event) {

    }

    @FXML
    void saveFirstCity(ActionEvent event) {
        final String value = firstCbox.getValue();
        if (value != null) {
            final int start = value.indexOf(" ");
            final int end = value.indexOf(",");
            this.firstCity = value.substring(start + 1, end);
        }
    }

    @FXML
    void saveSecondCity(ActionEvent event) {
        final String value = secondCbox.getValue();
        if (value != null) {
            final int start = value.indexOf(" ");
            final int end = value.indexOf(",");
            this.secondCity = value.substring(start + 1, end);
        }
    }

    @FXML
    void saveThirdCity(ActionEvent event) {
        final String value = thirdCbox.getValue();
        if (value != null) {
            final int start = value.indexOf(" ");
            final int end = value.indexOf(",");
            this.thirdCity = value.substring(start + 1, end);
        }
    }

    // ========================================CityMap=================================================
    @FXML
    void showCityMap(ActionEvent event) {
        if (UserInfo.selectedFlight != null) {
            try {
                // change the scene to homescene
                mainPane.getScene()
                        .setRoot(FXMLLoader.load(getClass().getResource(ClientApplication.FLIGHT_INFO_PATH)));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            ClientApplication.displayWarining("Not choosed flight",
                    "Please choose one flight to see more information abount it!", mainPane.getScene().getWindow());
        }
    }

    // ========================================UserFlightList=================================================
    @FXML
    void saveFlightInList(ActionEvent event) {
        if (UserInfo.user == null) {
            ClientApplication.displayWarining("Please Loggin",
                    "You need to login to save the flight in the list", mainPane.getScene().getWindow());
            return;
        }
        if (UserInfo.selectedFlight == null) {
            ClientApplication.displayWarining("Please choose a flight",
                    "You need to click one flight and then u can save the flight in the list",
                    mainPane.getScene().getWindow());
            return;
        }
        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("flight/user-list")
                        .queryParam("userId", UserInfo.user.getId())
                        .queryParam("flightId", UserInfo.selectedFlight.getId())
                        .build())
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorStop()
                .block();
        getUserFlightList();
    }

    enum TripModel {
        First, Second
    }

}
