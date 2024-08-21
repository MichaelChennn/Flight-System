package de.tum.nihaoshijie.flightsystem.server.repository;

import de.tum.nihaoshijie.flightsystem.server.persistence.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {
    @Query("select w from Weather w where w.city.id = :cityId and w.date = :date")
    Weather findWeatherByCityAndAndDate(long cityId, LocalDate date);
}