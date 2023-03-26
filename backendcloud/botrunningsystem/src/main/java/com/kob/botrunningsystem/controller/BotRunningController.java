package com.kob.botrunningsystem.controller;

import com.kob.botrunningsystem.service.BotRunningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/24 16:19
 */
@RestController
public class BotRunningController {

    @Autowired
    BotRunningService botRunningService;

    @PostMapping("/bot/add/")
    public String addBot(@RequestParam MultiValueMap<String , String > data){
        int user_id = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        String bot_code = data.getFirst("bot_code");
        String input = data.getFirst("input");
        return botRunningService.addBot(user_id,bot_code,input);
    }
}
