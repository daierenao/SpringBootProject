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
import com.kob.backend.consumer.utils.Player;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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

    public static UserMapper userMapper;
    private static BotMapper botMapper;
    public static RecordMapper recordMapper;
    //实现rest进程间的通信，发送请求
    public static RestTemplate restTemplate;
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) { WebSocketServer.restTemplate = restTemplate;}
    @Autowired
    public void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper = userMapper;
    }
    @Autowired
    public void setBotMapper(BotMapper botMapper) {WebSocketServer.botMapper = botMapper;}
    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) { WebSocketServer.recordMapper = recordMapper;};
    //维护已经建立的所有链接，静态可以所有对象共享 线程安全
    public static final ConcurrentHashMap<Integer,WebSocketServer> users = new ConcurrentHashMap<>();
//    //匹配池
//    private static final CopyOnWriteArraySet<User> matchPool = new CopyOnWriteArraySet<>();
    private User user;
    private Session session = null; //维护WebSocket链接
    public GameMap gameMap = null; //每个链接都有一个gamemap

    private static String addPlayerUrl = "http://127.0.0.1:3001/player/add/";
    private static String removePlayerUrl = "http://127.0.0.1:3001/player/remove/";


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

    public static void startGame(Integer aId , Integer aBotId, Integer bId, Integer bBotId){
        //匹配成功后传入双方的id
        User a = userMapper.selectById(aId) , b = userMapper.selectById(bId);
        Bot aBot = botMapper.selectById(aBotId) , bBot = botMapper.selectById(bBotId);
        GameMap gameMap = new GameMap(
                13,
                14,
                20,
                a.getId(),
                aBot,
                b.getId(),
                bBot
        );

        gameMap.createMap();
        if(users.get(a.getId()) != null)
        users.get(a.getId()).gameMap = gameMap;
        if(users.get(b.getId()) != null)
        users.get(b.getId()).gameMap = gameMap;
        gameMap.start();

        JSONObject respGame = new JSONObject();

        respGame.put("a_id" , gameMap.getPlayerA().getId());
        respGame.put("b_id" , gameMap.getPlayerB().getId());
        respGame.put("a_sx",gameMap.getPlayerA().getSx());
        respGame.put("a_sy",gameMap.getPlayerA().getSy());
        respGame.put("b_sx",gameMap.getPlayerA().getSx());
        respGame.put("b_sy",gameMap.getPlayerA().getSy());
        respGame.put("map",gameMap.getG());


        JSONObject resA = new JSONObject();
        resA.put("event","start-matching");
        resA.put("opponent_username",b.getUsername());
        resA.put("opponent_photo",b.getPhoto());
        resA.put("game", respGame);
        if(users.get(a.getId()) != null)
        users.get(a.getId()).sendMessage(resA.toJSONString());

        JSONObject resB = new JSONObject();
        resB.put("event","start-matching");
        resB.put("opponent_username",a.getUsername());
        resB.put("opponent_photo",a.getPhoto());
        resB.put("game", respGame);
        if(users.get(b.getId()) != null)
        users.get(b.getId()).sendMessage(resB.toJSONString());

    }
    //将用户加入匹配池
    public void startMatching(Integer bot_id){
        System.out.println("start matching!");
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("user_id",this.user.getId().toString());
        data.add("rating",this.user.getRating().toString());
        data.add("bot_id",bot_id.toString());
        restTemplate.postForObject(addPlayerUrl,data,String.class);

    }
    public void stopMatching(){
        System.out.println("stop matching!");
        MultiValueMap<String , String > data = new LinkedMultiValueMap<>();
        data.add("user_id",this.user.getId().toString());
        restTemplate.postForObject(removePlayerUrl,data,String.class);
    }
    //接收前端输入进行移动
    public void move(int direction){
        if(gameMap.getPlayerA().getId().equals(user.getId())){
            if(gameMap.getPlayerA().getBotId().equals(-1)) //如果是人 则接收前端控制
            gameMap.setNextStepA(direction);
        }
        else if(gameMap.getPlayerB().getId().equals(user.getId())){
            if(gameMap.getPlayerB().getBotId().equals(-1))
            gameMap.setNextStepB(direction);
        }
    }
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("receive message!");
        // 从Client接收消息
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if("start-matching".equals(event)){
            startMatching(data.getInteger("bot_id"));
        }
        else if("stop-matching".equals(event)){
            stopMatching();
        }
        else if("move".equals(event)){
            move(data.getInteger("direction"));
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
