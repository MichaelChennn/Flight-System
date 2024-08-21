package de.tum.nihaoshijie.flightsystem.server.repository;

import de.tum.nihaoshijie.flightsystem.server.persistence.Poi;
import de.tum.nihaoshijie.flightsystem.server.persistence.UserPoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserPoiRepository extends JpaRepository<UserPoi, Long> {
    @Query("select up.poi from UserPoi up where up.user.id = :userId")
    List<Poi> findPoisByUserId(@Param("userId") long userId);

}