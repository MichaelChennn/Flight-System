package de.tum.nihaoshijie.flightsystem.server.business.survey;

import de.tum.nihaoshijie.flightsystem.server.persistence.Survey;
import de.tum.nihaoshijie.flightsystem.server.persistence.User;
import de.tum.nihaoshijie.flightsystem.server.persistence.UserSurvey;
import de.tum.nihaoshijie.flightsystem.server.repository.UserRepository;
import de.tum.nihaoshijie.flightsystem.server.repository.survey.SurveyRepository;
import de.tum.nihaoshijie.flightsystem.server.repository.survey.UserSurveyRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SurveyService {
    private SurveyRepository surveyRepository;
    private UserSurveyRepository userSurveyRepository;
    private UserRepository userRepository;

    public SurveyService(SurveyRepository surveyRepository, UserRepository userRepository, UserSurveyRepository userSurveyRepository) {
        this.surveyRepository = surveyRepository;
        this.userRepository = userRepository;
        this.userSurveyRepository = userSurveyRepository;
    }

    public List<Survey> findSurveyByTheme(String theme) {
        return surveyRepository.findSurveyByTheme(theme);
    }

    public List<Survey> findAllSurveys() {
        return surveyRepository.findAll();
    }

    public void saveOrUpdateEvaluation(Survey survey) {
        surveyRepository.save(survey);
    }

    public void addSurvey(String name, int surveyId, double rate, String timeStamp, String comment) {
        Optional<User> user = userRepository.findUserByUserName(name);
        Optional<Survey> survey = surveyRepository.findSurveyById(Long.valueOf(surveyId));
        if (user.isEmpty() || survey.isEmpty()) {
            throw new IllegalArgumentException("User or survey is not found in database");
        } else {
            userSurveyRepository.save(new UserSurvey(user.orElse(null), survey.orElse(null), rate, timeStamp, comment));
        }
    }

}
