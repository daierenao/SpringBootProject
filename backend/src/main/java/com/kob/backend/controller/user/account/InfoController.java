package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description 登录成功后通过token上下文获取登录用户信息
 * @date 2023/3/10 12:02
 */
@RestController
public class InfoController {

    @Autowired
    InfoService infoService;

    @GetMapping("/user/account/info/")
    public Map<String,String> getInfo(){
        return infoService.getInfo();
    }

}
