package de.tum.nihaoshijie.flightsystem.server.repository;

import de.tum.nihaoshijie.flightsystem.server.persistence.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    @Query("select f from Flight f, City c1, City c2, Airport a1, Airport a2 " +
            "where f.depCity.id = c1.id and f.arrCity.id = c2.id and f.depAirport.id = a1.id " +
            "and f.arrAirport.id = a2.id and f.flightDate = :date and c1.name = :origin " +
            "and c2.name = :destination")
    List<Flight> findFlightsByDepCityAndArrCityAndFlightDate(@Param("origin") String origin,
                                                             @Param("destination") String destination,
                                                             @Param("date") LocalDate date);

    @Query("select f from Flight f where f.id in :ids")
    List<Flight> findFlightsByIds(@Param("ids") List<Long> ids);
}