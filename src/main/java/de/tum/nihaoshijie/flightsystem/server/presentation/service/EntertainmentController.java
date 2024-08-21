package de.tum.nihaoshijie.flightsystem.server.presentation.service;

import de.tum.nihaoshijie.flightsystem.server.business.service.EntertainmentService;
import de.tum.nihaoshijie.flightsystem.server.persistence.Entertainment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EntertainmentController {
    private EntertainmentService entertainmentService;

    @Autowired
    public EntertainmentController(EntertainmentService entertainmentService) {
        this.entertainmentService = entertainmentService;
    }

    //This endpoint fetches survey by name and type
    @GetMapping( "/entertainment")
    @ResponseBody
    public ResponseEntity<List<Entertainment>> getEntertainmentByNameAndType(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "type") String type) {
        return ResponseEntity.ok(entertainmentService.getEntertainmentByNameAndType(name, type));
    }

    //This endpoint fetches all surveys
    @GetMapping( "/entertainment/all")
    @ResponseBody
    public ResponseEntity<List<Entertainment>> getAllEntertainments() {
        return ResponseEntity.ok(entertainmentService.getAllEntertainment());
    }

    //This endpoint creates or updates an entertainment, not for client
    @PostMapping("/entertainment")
    @ResponseBody
    public void saveEntertainment(@RequestBody Entertainment entertainment) {
        entertainmentService.addEntertainment(entertainment);
    }
}
