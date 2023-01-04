package com.kob.backend.controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/1/4 11:56
 */

@RestController
@RequestMapping("/pk/")
public class BotInfoController {
    @RequestMapping("getbotinfo/")
    public List<Map<String,String>> getRecord(){
        List<Map<String,String>> list = new LinkedList<>();
        Map<String,String> bot1 = new HashMap<>();
        Map<String,String> bot2 = new HashMap<>();
        bot1.put("name","scott");
        bot1.put("name2","ssss");
        bot2.put("name","tiger");
        list.add(bot1);
        list.add(bot2);
        return list;
    }
}
