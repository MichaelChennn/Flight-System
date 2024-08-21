package de.tum.nihaoshijie.flightsystem.server.repository.survey;

import de.tum.nihaoshijie.flightsystem.server.persistence.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {
    @Query
    List<Reward> findRewardByName(String name);
}
