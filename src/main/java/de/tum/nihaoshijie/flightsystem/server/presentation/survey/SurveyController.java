package de.tum.nihaoshijie.flightsystem.server.presentation.survey;

import de.tum.nihaoshijie.flightsystem.server.business.survey.SurveyService;
import de.tum.nihaoshijie.flightsystem.server.persistence.Survey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class SurveyController {

    private SurveyService surveyService;

    @Autowired
    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    //This endpoint fetches survey by theme
    @GetMapping("/survey")
    @ResponseBody
    public ResponseEntity<List<Survey>> getSurveys(@RequestParam("theme") String theme) {
        return ResponseEntity.ok(surveyService.findSurveyByTheme(theme));
    }

    //This endpoint fetches all surveys
    @GetMapping("/survey/all")
    @ResponseBody
    public ResponseEntity<List<Survey>> getAllSurveys() {
        return ResponseEntity.ok(surveyService.findAllSurveys());
    }

    //This endpoint creates or updates a survey
    @PostMapping("/survey")
    @ResponseBody
    public void createEvaluation(@RequestBody Survey survey) {
        surveyService.saveOrUpdateEvaluation(survey);
    }

    //This endpoint creates or updates a user_survey relationship
    //Example:
    //POST localhost:8080/survey/user?name=LisaY&rate=8.0&timeStamp=2019-10-01T08:25:24.00Z&comment="not bad"&surveyId=1
    @PostMapping("/survey/user")
    @ResponseBody
    public void saveSurvey(@RequestParam("name") String name,
                           @RequestParam("surveyId") int surveyId,
                           @RequestParam("rate") double rate,
                           @RequestParam("timeStamp") String timeStamp,
                           @RequestParam("comment") String comment) {
        surveyService.addSurvey(name, surveyId, rate, timeStamp, comment);
    }
}
