package de.tum.nihaoshijie.flightsystem.server.repository;

import de.tum.nihaoshijie.flightsystem.server.persistence.UserFlight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFlightRepository extends JpaRepository<UserFlight, Long> {
    @Query("select uf.flight.id from UserFlight uf where uf.user.id = :userId")
    List<Long> findUserFlightsByUserId(@Param("userId") long userId);
}