package de.tum.nihaoshijie.flightsystem.server.repository.survey;

import de.tum.nihaoshijie.flightsystem.server.persistence.Survey;
import de.tum.nihaoshijie.flightsystem.server.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
    @Query
    List<Survey> findSurveyByTheme(String theme);

    @Query
    Optional<Survey> findSurveyById(Long id);

}
