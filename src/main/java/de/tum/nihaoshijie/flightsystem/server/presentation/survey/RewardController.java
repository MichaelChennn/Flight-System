package de.tum.nihaoshijie.flightsystem.server.presentation.survey;

import de.tum.nihaoshijie.flightsystem.server.business.survey.RewardService;
import de.tum.nihaoshijie.flightsystem.server.persistence.Reward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RewardController {
    private RewardService rewardService;

    @Autowired
    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    //This endpoint fetches reward by name
    @GetMapping("/reward")
    @ResponseBody
    public ResponseEntity<Reward> getReward(@RequestParam("name") String name) {
        return ResponseEntity.ok(rewardService.getRewardByName(name));
    }

    //This endpoint fetches all rewards
    @GetMapping("/reward/all")
    @ResponseBody
    public ResponseEntity<List<Reward>> getAllReward() {
        return ResponseEntity.ok(rewardService.findAllReward());
    }

    //This endpoint saves a reward to a user
    //Example:
    //POST localhost:8080/reward/user?name=amyZ&rewardName=Beer Coupon&timeStamp=2019-10-01T08:25:24.00Z
    @PostMapping("/reward/user")
    @ResponseBody
    public void saveReward(@RequestParam("name") String name,
                           @RequestParam("rewardName") String rewardName,
                           @RequestParam("timeStamp") String timeStamp) {
        rewardService.addReward(name, rewardName, timeStamp);
    }

    //This endpoint creates or updates a reward, not for client
    @PostMapping("/reward")
    @ResponseBody
    public void saveReward(@RequestBody Reward reward) {
        rewardService.saveReward(reward);
    }

}
