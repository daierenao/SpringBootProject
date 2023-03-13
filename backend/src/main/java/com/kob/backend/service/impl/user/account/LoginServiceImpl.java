package com.kob.backend.service.impl.user.account;

import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.LoginService;
import com.kob.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description 登录，验证jwt_token令牌是否可以登录
 * @date 2023/3/9 22:47
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> getToken(String username, String password) {
        //将用户名密码封装成token进行验证
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(username,password);
        //如果登录失败，则自动处理
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
        User user = loginUser.getUser();

        String jwt_token = JwtUtil.createJWT(user.getId().toString());

        Map<String,String> map = new HashMap<>();
        map.put("error_message","success");
        map.put("token",jwt_token);
        return map;
    }
}
