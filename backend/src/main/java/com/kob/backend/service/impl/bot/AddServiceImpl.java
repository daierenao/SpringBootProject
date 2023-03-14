package com.kob.backend.service.impl.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.bot.AddService;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/14 10:40
 */
@Service
public class AddServiceImpl implements AddService {

    @Autowired
    BotMapper botMapper;

    @Override
    public Map<String, String> add(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();

        User user = loginUser.getUser();
        Map<String,String> map= new HashMap<>();

        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

        if(title == null || title.length() == 0){
            map.put("error_message","标题不能为空");
            return map;
        }
        if(title.length() > 100){
            map.put("error_message","标题长度不能大于100");
            return map;
        }
        if(description == null || description.length() == 0){
            description = "该用户很烂，什么也没留下";
        }
        if(description.length() > 300){
            map.put("error_message","描述长度不能大于100");
            return map;
        }
        if(content == null || content.length() == 0){
            map.put("error_message","代码不能为空");
            return map;
        }
        if(content.length() > 10000){
            map.put("error_message","代码长度不能超过10000");
            return map;
        }
        Date now = new Date();

        Bot bot = new Bot(null,user.getId(),title,description,content,1500,now,now);

        botMapper.insert(bot);
        map.put("error_message","success");
        return map;
    }
}
