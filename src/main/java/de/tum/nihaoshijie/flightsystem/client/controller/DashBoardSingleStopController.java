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

/**
 * The Controller of TripDashboard
 */
public class DashBoardSingleStopController {
    /**
     * =================================================GUIcomponent============================================
     * Main pane of this scene
     */
    @FXML
    private AnchorPane mainPane;
    /**
     * Backbutton
     */
    @FXML
    private JFXButton backButton;
    /**
     * Button switch to Mutistop mode scene
     */
    @FXML
    private JFXButton mutiStopButton;
    /**
     * Search Button
     */
    @FXML
    private JFXButton searchButton;
    /**
     * the button to show the city map
     */
    @FXML
    private JFXButton mapButton;
    /**
     * First datepicker for single trip
     */
    @FXML
    private DatePicker firstDatePicker;
    /**
     * Departure combobox
     */
    @FXML
    private JFXComboBox<String> firstCbox;
    /**
     * Destination combobox
     */
    @FXML
    private JFXComboBox<String> secondCbox;
    /**
     * Treetableview of searched results
     */
    @FXML
    private JFXTreeTableView<FlightProperty> flightsTreetableView;
    /**
     * Favorite flight list
     */
    @FXML
    private JFXComboBox<String> userFlightListCbox;
    /**
     * Tooltip for firstCbox
     */
    @FXML
    private Tooltip depToolTip;
    /**
     * Tooltip for secondCbox
     */
    @FXML
    private Tooltip desToolTip;
    /**
     * Tooltip for userFlightCbox
     */
    @FXML
    private Tooltip userFlightTooltip;
    /**
     * the button to save a flight in the list
     */
    @FXML
    private JFXButton likeButton;

    /**
     * =================================================classattribute========================================
     * Save user city input string for search
     */
    private String departure;
    private String destination;
    /**
     * Citylist shows in combobox
     */
    private ObservableList<String> deporiginlist;
    private ObservableList<String> desoriginlist;
    private ObservableList<String> userFlightList;
    private ObservableList<TreeItem<FlightProperty>> selectedItems;
    /**
     * Attributes for the combobox
     */
    private String depFilter = "";
    private String desFilter = "";
    private String userFlightListFilter = "";
    /**
     * Save user date input.
     */
    private LocalDate firstDate;

    /**
     * generate a webclient
     */
    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    /**
     * root of the tree view
     */
    private FlightProperty rootProperty = new FlightProperty("flightDate", "depCity", "arrCity", "depAirport",
            "depTerminal", "depGate", "depTimeEst", "arrAirport", "arrTerminal", "arrGate",
            "arrTimeEst", "airline", "airplane", "FlightCode", null, null, null);
    private TreeItem<FlightProperty> root = new TreeItem<>(rootProperty);

    // ========================================Initialization=================================================

    /**
     * This method run before all the others method initialize all components
     *
     * @throws IOException
     */
    public void initialize() throws IOException {
        // 1.initialize the user flight list
        initializeUserFlightList();
        // 2.initialize items in comboboxs
        initializeListOfbox(firstCbox, secondCbox);
        // 3.initialize eventhandler of comboboxs
        initializeCombobox(firstCbox, secondCbox);
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

    /**
     * This method will initialize items in combobox
     */
    private void initializeListOfbox(JFXComboBox<String> firstCbox, JFXComboBox<String> secondCbox) {
        // add items in the list
        // TODO: create a list of all cities name and add it in comboboxs

        firstCbox.getItems().addAll("NKG Nanjing, CN", "PVG Shanghai, CN", "SHA Shanghai, CN", "MUC Munich, DE", "LIS Lisbon, PT");
        secondCbox.getItems().addAll("NKG Nanjing, CN", "PVG Shanghai, CN", "SHA Shanghai, CN", "MUC Munich, DE", "LIS Lisbon, PT");
        // save the list
        deporiginlist = FXCollections.observableArrayList(firstCbox.getItems());
        desoriginlist = FXCollections.observableArrayList(secondCbox.getItems());
    }

    /**
     * Initialization of comboboxs
     */
    private void initializeCombobox(JFXComboBox<String> firstCbox, JFXComboBox<String> secondCbox) {
        // 1. set the destination combobox disable at first, before the departure has
        // been choosed
        secondCbox.setDisable(true);
        // 2. set a tooltip
        firstCbox.setTooltip(depToolTip);
        secondCbox.setTooltip(desToolTip);
        // 3. set ket pressed event handler
        firstCbox.setOnKeyPressed(this::handleOnKeyPressedDep);
        secondCbox.setOnKeyPressed(this::handleOnKeyPressedDes);
        // 4. set sellfactor

    }

    /**
     * Initialize all the treetablecolum and layout
     */
    private void initializeTreeTableView() {
        /**
         * Treetableview layout
         */
        flightsTreetableView.setColumnResizePolicy(JFXTreeTableView.CONSTRAINED_RESIZE_POLICY);
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

        flightsTreetableView.getColumns().add(dateColum);
        flightsTreetableView.getColumns().add(airLineColum);
        flightsTreetableView.getColumns().add(airPortColumn);
        flightsTreetableView.getColumns().add(dep_Time_Column);
        flightsTreetableView.getColumns().add(dep_City_Column);
        flightsTreetableView.getColumns().add(arr_Airport_Column);
        flightsTreetableView.getColumns().add(arr_Time_Column);
        flightsTreetableView.getColumns().add(arr_City_Column);
        flightsTreetableView.getColumns().add(iCao_Column);
        flightsTreetableView.setRoot(root);
        flightsTreetableView.setShowRoot(false);
    }

    // =========================================================Eventhandle=======================================================

    /**
     * Event handle backbutton
     *
     * @param event
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

    /**
     * Click muti-stop button change to the muti-stop scene.
     *
     * @param event
     */
    @FXML
    void handleMutiStopButton(ActionEvent event) {
        try {
            // remove the saved flight
            UserInfo.selectedFlight = null;
            UserInfo.selectedFlightMutiStop = null;
            // change the scene to muti-stop
            mainPane.getScene()
                    .setRoot(FXMLLoader.load(getClass().getResource(ClientApplication.MUTI_TRIPDASHBOARD_PATH)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Event handle first datepicker
     *
     * @param event
     */
    @FXML
    void handleFirstDatepicker(ActionEvent event) {
        if (firstDatePicker.getValue() != null) {
            this.firstDate = firstDatePicker.getValue();
            // System.out.println(firsDate);
        }

    }

    /**
     * Save the selected Flight in the UserInfo class
     *
     * @param event mouse click on a row of treetableview
     */
    @FXML
    void handleOnSelectFLights(MouseEvent event) {
        selectedItems = flightsTreetableView.getSelectionModel().getSelectedItems();
        if (selectedItems.size() == 0) {
            mapButton.setDisable(true);
            likeButton.setDisable(true);
            return;
        } else {
            mapButton.setDisable(false);
            likeButton.setDisable(false);
            UserInfo.selectedFlight = selectedItems.get(0).getValue();
        }
    }

    /**
     * =========================================================SearchPart=======================================================
     * Event handle search button
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void handleSearchButtonClick(ActionEvent event) throws IOException {
        if (this.departure == null) {
            ClientApplication.displayWarining("Departure not found",
                    "Please choose one departure of your trip!", mainPane.getScene().getWindow());
            return;
        }
        if (this.destination == null) {
            ClientApplication.displayWarining("Destination not found",
                    "Please choose one destination of your trip!", mainPane.getScene().getWindow());
            return;
        }
        if (firstDate == null) {
            ClientApplication.displayWarining("Date not found",
                    "Please choose the date of your trip!", mainPane.getScene().getWindow());
            return;
        }

        /**
         * display all the search result in the list view
         */

        if (root != null && root.getChildren().size() != 0) {
            root.getChildren().clear();
        }
        mapButton.setDisable(true);
        likeButton.setDisable(true);
        getAllSearchedFlight();

    }

    /**
     * Send request to server and add all flightmodel in the list
     *
     * @throws IOException
     */
    private void getAllSearchedFlight() throws IOException {
        /**
         * call the findFlightsByOriginAndDestinationWithDate in flightcontroller class.
         */

        List<Flight> flights = this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("flight")
                        .queryParam("origin", departure)
                        .queryParam("destination", destination)
                        .queryParam("date", firstDate.toString())
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Flight>>() {
                })
                .block();
        setFlights(flights);

    }

    /**
     * Consumer method to add searched flight data in treetable view
     *
     * @param flights
     */
    private void setFlights(List<Flight> flights) {
        if (flights.size() == 0) {
            ClientApplication.displayWarining("Flights not found",
                    "We haven't found any flights on this date. Please choose another date",
                    mainPane.getScene().getWindow());
            return;
        }
        Platform.runLater(() -> {
            for (Flight flight : flights) {
                root.getChildren()
                        .add(new TreeItem<FlightProperty>(new FlightProperty(flight.getFlightDate().toString(),
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

    /**
     * =========================================================TextFieldInputPart=======================================================
     * Event handler for departure combobox
     *
     * @param event
     */
    @FXML
    void handleFirstDepCbox(ActionEvent event) {
        final String value = firstCbox.getValue();
        if (value != null) {
            // user can't choose destiantion until choose the departure
            secondCbox.setDisable(false);
            final int start = value.indexOf(" ");
            final int end = value.indexOf(",");
            this.departure = value.substring(start + 1, end);
        }
    }

    /**
     * Event handle destination combobox
     *
     * @param event
     */
    @FXML
    void handleFirstDesCbox(ActionEvent event) {
        final String value = secondCbox.getValue();
        if (value != null) {
            final int start = value.indexOf(" ");
            final int end = value.indexOf(",");
            this.destination = value.substring(start + 1, end);
        }

    }

    /**
     * Sort the departure cities with user input
     *
     * @param event keyboard input
     */
    @FXML
    void handleOnKeyPressedDep(KeyEvent event) {
        ObservableList<String> filteredList = FXCollections.observableArrayList();
        KeyCode code = event.getCode();
        // get user input and save it in filter
        if (code.isLetterKey()) {
            depFilter += event.getText();
        }
        // edit the tooltips when user press back space
        if (code == KeyCode.BACK_SPACE && depFilter.length() > 0) {
            depFilter = depFilter.substring(0, depFilter.length() - 1);
            firstCbox.getItems().setAll(deporiginlist);
        }

        if (code == KeyCode.ESCAPE) {
            depFilter = "";
        }
        // hide the tooltips when there is no user input
        if (depFilter.length() == 0) {
            filteredList = deporiginlist;
            depToolTip.hide();
        } else {
            // show the correct content with tooltip;
            Stream<String> itens = firstCbox.getItems().stream();
            String txtUsr = depFilter.toString().toLowerCase();
            // sort the items in combobox
            itens.filter(el -> el.toString().toLowerCase().contains(txtUsr)).forEach(filteredList::add);
            depToolTip.setText(txtUsr);
            Window stage = firstCbox.getScene().getWindow();

            double posX = stage.getX() + firstCbox.getBoundsInParent().getMinX();
            double posY = stage.getY() + firstCbox.getBoundsInParent().getMinY();

            depToolTip.setFont(new Font("Arial", 11));
            depToolTip.show(stage, posX, posY);
            firstCbox.show();

        }
        firstCbox.getItems().setAll(filteredList);
    }

    /**
     * Event handle of hiding deptooltips
     *
     * @param event
     */
    @FXML
    void handleOnHidingDep(Event event) {
        depFilter = "";
        depToolTip.hide();
        String s = firstCbox.getSelectionModel().getSelectedItem();
        firstCbox.getItems().setAll(deporiginlist);
        firstCbox.getSelectionModel().select(s);
    }

    /**
     * Sort the destination cities with user input
     *
     * @param event keyboard input
     */
    @FXML
    void handleOnKeyPressedDes(KeyEvent event) {
        ObservableList<String> filteredList = FXCollections.observableArrayList();
        KeyCode code = event.getCode();
        // get user input and save it in filter
        if (code.isLetterKey()) {
            desFilter += event.getText();
        }
        // edit the tooltips when user press back space
        if (code == KeyCode.BACK_SPACE && desFilter.length() > 0) {
            desFilter = desFilter.substring(0, desFilter.length() - 1);
            secondCbox.getItems().setAll(desoriginlist);
        }
        if (code == KeyCode.ESCAPE) {
            desFilter = "";
        }
        // hide the tooltips when there is no user input
        if (desFilter.length() == 0) {
            filteredList = desoriginlist;
            desToolTip.hide();
        } else {
            // show the correct content with tooltip;
            Stream<String> itens = secondCbox.getItems().stream();
            String txtUsr = desFilter.toString().toLowerCase();
            // sort the items in combobox
            itens.filter(el -> el.toString().toLowerCase().contains(txtUsr)).forEach(filteredList::add);
            desToolTip.setText(txtUsr);
            Window stage = secondCbox.getScene().getWindow();

            double posX = stage.getX() + secondCbox.getBoundsInParent().getMinX();
            double posY = stage.getY() + secondCbox.getBoundsInParent().getMinY();

            desToolTip.setFont(new Font("Arial", 11));
            desToolTip.show(stage, posX, posY);
            secondCbox.show();

        }
        secondCbox.getItems().setAll(filteredList);
    }

    /**
     * Eventhandler of hiding destooltips
     *
     * @param event
     */
    @FXML
    void handleOnHidingDes(Event event) {
        desFilter = "";
        desToolTip.hide();
        String s = secondCbox.getSelectionModel().getSelectedItem();
        secondCbox.getItems().setAll(desoriginlist);
        secondCbox.getSelectionModel().select(s);
    }

    /**
     * Sort the user flight list with user input
     *
     * @param event
     */
    @FXML
    void handleUserListCbox(KeyEvent event) {
        ObservableList<String> filteredList = FXCollections.observableArrayList();

        KeyCode code = event.getCode();
        // get user input and save it in filter
        if (code.isLetterKey()) {
            userFlightListFilter += event.getText();
        }
        // edit the tooltips when user press back space
        if (code == KeyCode.BACK_SPACE && userFlightListFilter.length() > 0) {
            userFlightListFilter = userFlightListFilter.substring(0, userFlightListFilter.length() - 1);
            userFlightListCbox.getItems().setAll(userFlightList);
        }
        if (code == KeyCode.ESCAPE) {
            userFlightListFilter = "";
        }
        // hide the tooltips when there is no user input
        if (userFlightListFilter.length() == 0) {
            filteredList = userFlightList;
            userFlightTooltip.hide();
        } else {
            // show the correct content with tooltip;
            Stream<String> itens = userFlightListCbox.getItems().stream();
            String txtUsr = userFlightListFilter.toString().toLowerCase();
            // sort the items in combobox
            itens.filter(el -> el.toString().toLowerCase().contains(txtUsr)).forEach(filteredList::add);
            userFlightTooltip.setText(txtUsr);
            Window stage = secondCbox.getScene().getWindow();

            double posX = stage.getX() + userFlightListCbox.getBoundsInParent().getMinX();
            double posY = stage.getY() + userFlightListCbox.getBoundsInParent().getMinY();

            userFlightTooltip.setFont(new Font("Arial", 11));
            userFlightTooltip.show(stage, posX, posY);
            userFlightListCbox.show();

        }
        userFlightListCbox.getItems().setAll(filteredList);
    }

    /**
     * Eventhandler of hiding userflightlisttooltips
     *
     * @param event
     */
    @FXML
    public void handleOnHidingUserFlightList(Event event) {
        userFlightListFilter = "";
        userFlightTooltip.hide();
        String s = userFlightListCbox.getSelectionModel().getSelectedItem();
        userFlightListCbox.getItems().setAll(userFlightList);
        userFlightListCbox.getSelectionModel().select(s);
    }

    /**
     * =======================================================CityMap=============================================
     * Display a city map of the selected city from the treetableview
     *
     * @param event click the map button
     */
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

    /**
     * ======================================================UserFlightList===============================================
     * Save a selected flight in user flight list
     *
     * @param event click the save button
     */
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

}
