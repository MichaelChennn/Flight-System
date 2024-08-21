package de.tum.nihaoshijie.flightsystem.server.business.survey;

import de.tum.nihaoshijie.flightsystem.common.model.survey.RewardModel;
import de.tum.nihaoshijie.flightsystem.server.persistence.*;
import de.tum.nihaoshijie.flightsystem.server.repository.UserRepository;
import de.tum.nihaoshijie.flightsystem.server.repository.survey.RewardRepository;
import de.tum.nihaoshijie.flightsystem.server.repository.survey.UserRewardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RewardService {
    private RewardRepository rewardRepository;
    private UserRepository userRepository;
    private UserRewardRepository userRewardRepository;
    @Autowired
    public RewardService(RewardRepository rewardRepository, UserRepository userRepository, UserRewardRepository userRewardRepository) {
        this.rewardRepository = rewardRepository;
        this.userRepository = userRepository;
        this.userRewardRepository = userRewardRepository;
    }

    public List<Reward> findAllReward() {
        return rewardRepository.findAll();
    }

    public Reward getRewardByName(String name) {
        return rewardRepository.findRewardByName(name).get(0);
    }

    public void saveReward(Reward reward)
    {
        rewardRepository.save(reward);
    }

    public void addReward(String name, String rewardName, String timeStamp) {
        Optional<User> user = userRepository.findUserByUserName(name);
        Reward reward = rewardRepository.findRewardByName(rewardName).get(0);
        if (user.isEmpty() || reward.equals(null)) {
            throw new IllegalArgumentException("User id or Reward is not found in database");
        } else {
            userRewardRepository.save(new UserReward(user.orElse(null), reward, timeStamp));
        }
    }

}
