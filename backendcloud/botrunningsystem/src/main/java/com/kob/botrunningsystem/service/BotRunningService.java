package com.kob.botrunningsystem.service;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/24 16:17
 */
public interface BotRunningService {
    //传入用户id bot代码 以及当前的游戏信息：地图，双方蛇的位置
    public String addBot(Integer userId , String botCode, String input);
}
