package de.tum.nihaoshijie.flightsystem.server.business.service;

import de.tum.nihaoshijie.flightsystem.server.persistence.InFlightService;
import de.tum.nihaoshijie.flightsystem.server.persistence.UserInflightservice;
import de.tum.nihaoshijie.flightsystem.server.repository.service.InFlightServiceRepository;
import de.tum.nihaoshijie.flightsystem.server.repository.service.UserInflightserviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InFlightServiceService {
    private InFlightServiceRepository inFlightServiceRepository;
    private UserInflightserviceRepository userInflightserviceRepository;

    @Autowired
    public InFlightServiceService(InFlightServiceRepository inFlightServiceRepository, UserInflightserviceRepository userInflightserviceRepository) {
        this.inFlightServiceRepository = inFlightServiceRepository;
        this.userInflightserviceRepository = userInflightserviceRepository;
    }

    public void addInFlightService(InFlightService inFlightService) {
        inFlightServiceRepository.save(inFlightService);
    }

    public List<InFlightService> getServiceByType(String type) {
        return inFlightServiceRepository.getInFlightServiceByType(type);
    }

    public List<InFlightService> getAllInFlightService() {
        return inFlightServiceRepository.findAll();
    }

    public Optional<InFlightService> findInFlightServiceByServiceId(long id) {
        return inFlightServiceRepository.findById(id);
    }

    public UserInflightservice saveUserInFlightService(UserInflightservice userInflightservice) {
        return userInflightserviceRepository.save(userInflightservice);
    }
}