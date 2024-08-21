package de.tum.nihaoshijie.flightsystem.server.business.service;

import de.tum.nihaoshijie.flightsystem.server.persistence.Entertainment;
import de.tum.nihaoshijie.flightsystem.server.repository.service.EntertainmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntertainmentService {

    private EntertainmentRepository entertainmentRepository;

    @Autowired
    public EntertainmentService (EntertainmentRepository entertainmentRepository) {
        this.entertainmentRepository = entertainmentRepository;
    }

    public void addEntertainment(Entertainment entertainment) {
            entertainmentRepository.save(entertainment);
    }

    public List<Entertainment> getEntertainmentByNameAndType (String name, String type) {
        return entertainmentRepository.getEntertainmentByNameAndType(name, type);
    }



    public List<Entertainment> getAllEntertainment () {
        return entertainmentRepository.findAll();
    }
}
