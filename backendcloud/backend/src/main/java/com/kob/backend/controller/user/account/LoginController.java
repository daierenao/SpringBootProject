package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description 从前端传入用户名、密码 返回token
 * @date 2023/3/10 11:13
 */
@RestController
public class LoginController {

    @Autowired
    LoginService loginService;


    //通过post请求将用户名和密码传入后端，进行token验证，验证成功将token信息返回前端
    @PostMapping("/user/account/token/")
    public Map<String,String> getToken(@RequestParam Map<String,String> map){
        String username = map.get("username");
        String password = map.get("password");
        return loginService.getToken(username,password);
    }

}
