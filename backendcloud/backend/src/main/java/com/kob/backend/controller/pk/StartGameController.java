package com.kob.backend.controller.pk;

import com.kob.backend.service.pk.StartGameService;
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
 * @date 2023/3/23 21:28
 */
@RestController
public class StartGameController {
    @Autowired
    StartGameService startGameService;

    @PostMapping("/pk/start/game/")
    public String startGame(@RequestParam MultiValueMap<String,String> data){
        Integer a_id = Integer.parseInt(Objects.requireNonNull(data.getFirst("a_id")));
        Integer b_id = Integer.parseInt(Objects.requireNonNull(data.getFirst("b_id")));
        return startGameService.startGame(a_id,b_id);
    }
}
