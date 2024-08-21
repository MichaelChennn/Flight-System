package de.tum.nihaoshijie.flightsystem.server.business;

import de.tum.nihaoshijie.flightsystem.common.model.PoiModel;
import de.tum.nihaoshijie.flightsystem.server.persistence.Poi;
import de.tum.nihaoshijie.flightsystem.server.persistence.User;
import de.tum.nihaoshijie.flightsystem.server.persistence.UserPoi;
import de.tum.nihaoshijie.flightsystem.server.repository.PoiRepository;
import de.tum.nihaoshijie.flightsystem.server.repository.UserPoiRepository;
import de.tum.nihaoshijie.flightsystem.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PoiService {
    private PoiRepository poiRepository;
    private UserPoiRepository userPoiRepository;
    private UserRepository userRepository;

    @Autowired
    public PoiService(PoiRepository poiRepository, UserPoiRepository userPoiRepository, UserRepository userRepository) {
        this.poiRepository = poiRepository;
        this.userPoiRepository = userPoiRepository;
        this.userRepository = userRepository;
    }

    @Deprecated
    public List<PoiModel> getTopTenPois(String name) {
        // TODO: get pois
        // TODO: sort by rate
        // TODO: find top ten+
        return null;
    }

    public void savePoi(Poi poi) {
        poiRepository.save(poi);
    }

    public List<Poi> findAllPois() {
        return poiRepository.findAll();
    }

    public void addToUserList(long userid, long poiId) {
        final Optional<User> user = userRepository.findById(userid);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User id is not found in database");
        }
        final Optional<Poi> poi = poiRepository.findById(poiId);
        if (poi.isEmpty()) {
            throw new IllegalArgumentException("Poi id is not found in database");
        }
        userPoiRepository.save(new UserPoi(user.get(), poi.get()));
    }
}
