package com.kob.matchingsystem.service;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/22 23:00
 */
public interface MatchingService {
    String addPlayer(Integer userId, Integer rating,Integer bot_id);
    String removePlayer(Integer userId);
}
