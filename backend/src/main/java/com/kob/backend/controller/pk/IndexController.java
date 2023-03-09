package com.kob.backend.controller.pk;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/9 15:38
 */
@RestController
public class IndexController {
    @RequestMapping("/")
    public String index(){
        return "pk/index.html";
    }
}
