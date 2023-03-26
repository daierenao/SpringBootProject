package com.kob.botrunningsystem.service.impl;

import com.kob.botrunningsystem.service.BotRunningService;
import com.kob.botrunningsystem.service.impl.utils.BotPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/24 16:18
 */
@Service
public class BotRunningServiceImpl implements BotRunningService {

    public static RestTemplate restTemplate;
    public static final BotPool botPool = new BotPool();

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        BotRunningServiceImpl.restTemplate = restTemplate;
    }

    @Override
    public String addBot(Integer userId, String botCode, String input) {
        System.out.println("add bot successfully");
        botPool.addBot(userId,botCode,input);
        return "add bot successfully";
    }
}
