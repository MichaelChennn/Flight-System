package de.tum.nihaoshijie.flightsystem.server.presentation.service;

import de.tum.nihaoshijie.flightsystem.server.business.UserService;
import de.tum.nihaoshijie.flightsystem.server.business.service.InFlightServiceService;
import de.tum.nihaoshijie.flightsystem.server.persistence.InFlightService;
import de.tum.nihaoshijie.flightsystem.server.persistence.User;
import de.tum.nihaoshijie.flightsystem.server.persistence.UserInflightservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class InFlightServiceController {
    private InFlightServiceService inFlightServiceService;
    private UserService userService;

    @Autowired
    public InFlightServiceController(InFlightServiceService inFlightServiceService, UserService userService) {
        this.inFlightServiceService = inFlightServiceService;
        this.userService = userService;
    }

    //This endpoint fetches inFlightService by type
    @GetMapping("/inFlightService")
    @ResponseBody
    public ResponseEntity<List<InFlightService>> getInFlightServiceByType(@RequestParam("type") String type) {
        return ResponseEntity.ok(inFlightServiceService.getServiceByType(type));
    }

    //This endpoint fetches all inFlightServices
    @GetMapping("/inFlightService/all")
    @ResponseBody
    public ResponseEntity<List<InFlightService>> getAllInFlightService() {
        return ResponseEntity.ok(inFlightServiceService.getAllInFlightService());
    }

    //This endpoint creates or updates an inFlightService, not for client
    @PostMapping("/inFlightService")
    @ResponseBody
    public void saveInFlightService(@RequestBody InFlightService inFlightService) {
        inFlightServiceService.addInFlightService(inFlightService);
    }

    @PostMapping("/inFlightService/user-inflightservice")
    @ResponseBody
    public ResponseEntity<UserInflightservice> saveUserInFlightService(@RequestParam("userId") String userId, @RequestParam("serviceId") String serviceId) {
        final Optional<User> user = userService.findUserById(Long.parseLong(userId));
        final Optional<InFlightService> service = inFlightServiceService.findInFlightServiceByServiceId(Long.parseLong(serviceId));
        if(user.isEmpty() || service.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        final UserInflightservice userInflightservice = inFlightServiceService.saveUserInFlightService(new UserInflightservice(user.orElse(null), service.orElse(null), Instant.now()));
        if (userInflightservice == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userInflightservice);
    }
}
