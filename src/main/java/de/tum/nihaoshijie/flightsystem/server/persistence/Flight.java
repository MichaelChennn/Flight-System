package de.tum.nihaoshijie.flightsystem.server.persistence;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "flight")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "flight_date", nullable = false)
    private LocalDate flightDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dep_city_id")
    private City depCity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dep_airport_id")
    private Airport depAirport;

    @Column(name = "dep_terminal")
    private Integer depTerminal;

    @Column(name = "dep_gate", length = 10)
    private String depGate;

    @Column(name = "dep_time_sch")
    private Instant depTimeSch;

    @Column(name = "dep_time_est")
    private Instant depTimeEst;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arr_city_id")
    private City arrCity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arr_airport_id")
    private Airport arrAirport;

    @Column(name = "arr_terminal")
    private Integer arrTerminal;

    @Column(name = "arr_gate", length = 10)
    private String arrGate;

    @Column(name = "arr_time_sch")
    private Instant arrTimeSch;

    @Column(name = "arr_time_est")
    private Instant arrTimeEst;

    @Column(name = "icao", length = 10)
    private String icao;

    @Column(name = "airline", length = 20)
    private String airline;

    @Column(name = "airplane", length = 10)
    private String airplane;

    public Flight() {
    }

    public Flight(City origin, City destination, LocalDate flightDate) {
        this.depCity = origin;
        this.arrCity = destination;
        this.flightDate = flightDate;
    }

    public String getAirplane() {
        return airplane;
    }

    public void setAirplane(String airplane) {
        this.airplane = airplane;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getIcao() {
        return icao;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public Instant getArrTimeEst() {
        return arrTimeEst;
    }

    public void setArrTimeEst(Instant arrTimeEst) {
        this.arrTimeEst = arrTimeEst;
    }

    public Instant getArrTimeSch() {
        return arrTimeSch;
    }

    public void setArrTimeSch(Instant arrTimeSch) {
        this.arrTimeSch = arrTimeSch;
    }

    public String getArrGate() {
        return arrGate;
    }

    public void setArrGate(String arrGate) {
        this.arrGate = arrGate;
    }

    public Integer getArrTerminal() {
        return arrTerminal;
    }

    public void setArrTerminal(Integer arrTerminal) {
        this.arrTerminal = arrTerminal;
    }

    public Airport getArrAirport() {
        return arrAirport;
    }

    public void setArrAirport(Airport arrAirport) {
        this.arrAirport = arrAirport;
    }

    public City getArrCity() {
        return arrCity;
    }

    public void setArrCity(City arrCity) {
        this.arrCity = arrCity;
    }

    public Instant getDepTimeEst() {
        return depTimeEst;
    }

    public void setDepTimeEst(Instant depTimeEst) {
        this.depTimeEst = depTimeEst;
    }

    public Instant getDepTimeSch() {
        return depTimeSch;
    }

    public void setDepTimeSch(Instant depTimeSch) {
        this.depTimeSch = depTimeSch;
    }

    public String getDepGate() {
        return depGate;
    }

    public void setDepGate(String depGate) {
        this.depGate = depGate;
    }

    public Integer getDepTerminal() {
        return depTerminal;
    }

    public void setDepTerminal(Integer depTerminal) {
        this.depTerminal = depTerminal;
    }

    public Airport getDepAirport() {
        return depAirport;
    }

    public void setDepAirport(Airport depAirport) {
        this.depAirport = depAirport;
    }

    public City getDepCity() {
        return depCity;
    }

    public void setDepCity(City depCity) {
        this.depCity = depCity;
    }

    public LocalDate getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(LocalDate flightDate) {
        this.flightDate = flightDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}