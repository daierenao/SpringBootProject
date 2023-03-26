package com.kob.botrunningsystem.service.impl.utils;

import com.kob.botrunningsystem.service.impl.BotRunningServiceImpl;
import com.kob.botrunningsystem.service.impl.utils.Bot;
import com.kob.botrunningsystem.service.utils.BotInterface;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/24 22:33
 */
@Component
public class Consumer extends Thread{

    private static RestTemplate restTemplate;
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        Consumer.restTemplate = restTemplate;
    }
    private String receiveBotMoveUrl = "http://127.0.0.1:3000/pk/receive/bot/move/";
    private Bot bot;
    //设置线程处理时间
    public void startTimeOut(long timeOut, Bot bot){
        this.bot = bot;
        this.start();

        try {
            this.join(timeOut); //如果超过timeout则终止进程
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.interrupt();
    }

    private String addUUid(String uid , String botCode){
        int k = botCode.indexOf(" implements BotInterface");
        if(k == -1) return "";
        return botCode.substring(0,k) + uid  + botCode.substring(k);
    }

    @Override
    public void run() {
        UUID uuid = UUID.randomUUID();
        String uid = uuid.toString().substring(0,6);
        BotInterface botInterface = Reflect.compile(
                "com.kob.botrunningsystem.service.utils.Bot" + uid,
                addUUid(uid,bot.getBotCode())
        ).create().get();
        System.out.println(bot.getInput());
        Integer direction = botInterface.nextMove(bot.getInput());
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("userId",bot.getUserId().toString());
        data.add("direction",direction.toString());

        restTemplate.postForObject(receiveBotMoveUrl,data,String.class);
    }
}
