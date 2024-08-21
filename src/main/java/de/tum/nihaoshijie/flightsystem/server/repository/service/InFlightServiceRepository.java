package de.tum.nihaoshijie.flightsystem.server.repository.service;

import de.tum.nihaoshijie.flightsystem.server.persistence.InFlightService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InFlightServiceRepository extends JpaRepository<InFlightService, Long> {
    @Query("select e from InFlightService e " +
            "where e.type = :type")
    List<InFlightService> getInFlightServiceByType(@Param("type") String type);

}