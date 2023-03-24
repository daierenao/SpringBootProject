package com.kob.backend.controller.bot;

import com.kob.backend.service.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/14 12:17
 */
@RestController
public class AddController {
    @Autowired
    AddService addService;
    @PostMapping("/user/bot/add/")
    public Map<String,String> add(@RequestParam Map<String,String> data){
        return addService.add(data);
    }
}
