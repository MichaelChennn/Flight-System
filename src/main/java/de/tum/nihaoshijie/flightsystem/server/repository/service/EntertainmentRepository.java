package de.tum.nihaoshijie.flightsystem.server.repository.service;

import de.tum.nihaoshijie.flightsystem.server.persistence.Entertainment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntertainmentRepository extends JpaRepository<Entertainment, Long> {
    @Query(value = "select * from Entertainment e where name=:name and type=:type",
            nativeQuery = true)
    List<Entertainment> getEntertainmentByNameAndType(@Param("name") String name,
                                                             @Param("type") String type);
}