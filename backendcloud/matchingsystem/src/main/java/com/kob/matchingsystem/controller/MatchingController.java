package com.kob.matchingsystem.controller;

import com.kob.matchingsystem.service.MatchingService;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.MaskFormatter;
import java.util.Objects;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/23 16:30
 */
@RestController
public class MatchingController {

    @Autowired
    MatchingService matchingService;

    @PostMapping("/player/add/")
    public String addPlayer(@RequestParam MultiValueMap<String,String> data){
        Integer user_id = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        Integer rating = Integer.parseInt(Objects.requireNonNull(data.getFirst("rating")));
        return matchingService.addPlayer(user_id,rating);
    }
    @PostMapping("/player/remove/")
    public String removePlayer(@RequestParam MultiValueMap<String,String> data){
        Integer user_id = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        return matchingService.removePlayer(user_id);
    }
}
