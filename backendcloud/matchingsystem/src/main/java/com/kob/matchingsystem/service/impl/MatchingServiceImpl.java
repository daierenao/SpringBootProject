package com.kob.matchingsystem.service.impl;

import com.kob.matchingsystem.service.MatchingService;
import com.kob.matchingsystem.service.impl.utils.MatchingPool;
import com.kob.matchingsystem.service.impl.utils.Player;
import org.springframework.stereotype.Service;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/22 23:01
 */
@Service
public class MatchingServiceImpl implements MatchingService {

    public static final MatchingPool matchingpool = new MatchingPool();


    @Override
    public String addPlayer(Integer userId, Integer rating) {
        System.out.println("addPlayer: " + userId + " " + rating);
        matchingpool.addPlayer(userId,rating);
        return "addPlayer successfully";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("removePlayer: " + userId);
        matchingpool.removePlayer(userId);
        return "removePlayer successfully";
    }
}
