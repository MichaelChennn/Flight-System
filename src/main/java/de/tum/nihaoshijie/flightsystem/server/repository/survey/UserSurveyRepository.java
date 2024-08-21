package de.tum.nihaoshijie.flightsystem.server.repository.survey;

import de.tum.nihaoshijie.flightsystem.server.persistence.UserSurvey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSurveyRepository extends JpaRepository<UserSurvey, Long> {
}
