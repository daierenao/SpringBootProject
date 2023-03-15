package com.kob.backend.consumer;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/15 11:21
 */
import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.utils.GameMap;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    private static UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper = userMapper;
    }

    //维护已经建立的所有链接，静态可以所有对象共享 线程安全
    private static final ConcurrentHashMap<Integer,WebSocketServer> users = new ConcurrentHashMap<>();
    //匹配池
    private static final CopyOnWriteArraySet<User> matchPool = new CopyOnWriteArraySet<>();
    private User user;
    private Session session = null; //维护WebSocket链接

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session = session;
        System.out.println("connected");
        //通过token获取用户id
        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);

        if(this.user != null) {
            users.put(userId,this);
        }
        else {
            this.session.close();
        }
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("disconneted");
        if(this.user != null){
            users.remove(this.user.getId());
        }
    }

    public void startMatching(){
        System.out.println("start matching!");
        matchPool.add(this.user);

        while(matchPool.size() >= 2){
            Iterator<User> it = matchPool.iterator();
            User a = it.next() , b = it.next();
            matchPool.remove(a);
            matchPool.remove(b);

            GameMap gameMap = new GameMap(13, 14, 30);

            gameMap.createMap();

            JSONObject resA = new JSONObject();
            resA.put("event","start-matching");
            resA.put("opponent_username",b.getUsername());
            resA.put("opponent_photo",b.getPhoto());
            resA.put("gamemap", gameMap.getG());
            users.get(a.getId()).sendMessage(resA.toJSONString());

            JSONObject resB = new JSONObject();
            resB.put("event","start-matching");
            resB.put("opponent_username",a.getUsername());
            resB.put("opponent_photo",a.getPhoto());
            resB.put("gamemap", gameMap.getG());
            users.get(b.getId()).sendMessage(resB.toJSONString());


        }
    }
    public void stopMatching(){
        System.out.println("stop matching!");
        matchPool.remove(this.user);
    }
    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if("start-matching".equals(event)){
            startMatching();
        }
        else if("stop-matching".equals(event)){
            stopMatching();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message){
        synchronized (this.session){
            try{
                this.session.getBasicRemote().sendText(message);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
