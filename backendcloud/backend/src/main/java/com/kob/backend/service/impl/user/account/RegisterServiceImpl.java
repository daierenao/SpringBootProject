package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/9 22:47
 */
@Service
public class RegisterServiceImpl implements RegisterService {


    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) {
        Map<String,String> map = new HashMap<>();
        if(username == null){
            map.put("error_message","用户名不能为空");
            return map;
        }
        if(password == null || confirmedPassword == null){
            map.put("error_message","密码不能为空");
            return map;
        }
        //删除空格及空白字符
        username = username.trim();
        if(username.length() == 0){
            map.put("error_message","用户名不能为空");
            return map;
        }
        if(password.length() == 0 || confirmedPassword.length() == 0){
            map.put("error_message","密码不能为空");
            return map;
        }

        if(username.length() > 100){
            map.put("error_message","用户名长度不能超过100");
            return map;
        }
        if(password.length() > 100 || confirmedPassword.length() > 100){
            map.put("error_message","密码长度不能超过100");
            return map;
        }
        if(!password.equals(confirmedPassword)){
            map.put("error_message","两次输入的密码不一致");
            return map;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User userCheck = userMapper.selectOne(queryWrapper);
        if(userCheck != null){
            map.put("error_message","用户名已经存在");
            return map;
        }
        //判断结束 将注册用户加入数据库
        String encodedPassword = passwordEncoder.encode(password);
        String photo = "https://cdn.acwing.com/media/user/profile/photo/222088_lg_0990dabc2e.jpg";
        User user = new User(null, username, encodedPassword, photo,1500);
        userMapper.insert(user);

        map.put("error_message","success");
        return map;
    }
}
