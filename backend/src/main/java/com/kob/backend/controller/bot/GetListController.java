package com.kob.backend.controller.bot;

import com.kob.backend.pojo.Bot;
import com.kob.backend.service.bot.GetListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/14 14:01
 */
@RestController
public class GetListController {

    @Autowired
    private GetListService getListService;

    @GetMapping("/user/bot/getlist")
    List<Bot> getList(){
        return getListService.getList();
    }

}
