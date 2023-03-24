package com.kob.matchingsystem.service.impl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/23 20:32
 */
@Component
public class MatchingPool extends Thread{

    private static RestTemplate restTemplate;
    private static List<Player> players = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private static String startGameUrl = "http://127.0.0.1:3000/pk/start/game/";
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        MatchingPool.restTemplate = restTemplate;
    }

    private void checkAdded(Integer userId){
        for(Player player : players){
            if(player.getUserId().equals(userId))
            {
                players.remove(player);
                break;
            }
        }
    }

    public void addPlayer(Integer userId, Integer rating){
        lock.lock();
        try {
            //如果在等待队列 则删除 更新为新的
            checkAdded(userId);
            players.add(new Player(userId,rating,0));
        }finally {
            lock.unlock();
        }
    }

    public void removePlayer(Integer userId){
        lock.lock();
        try {
            List<Player> newPlayers = new ArrayList<>();
            for(Player player : players){
                if(!player.getUserId().equals(userId)){
                    newPlayers.add(player);
                }
            }
            players = newPlayers;
        }finally {
            lock.unlock();
        }
    }

    private void sendResult(Player a , Player b){
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("a_id",a.getUserId().toString());
        data.add("b_id",b.getUserId().toString());
        //向Web端发送http请求
        restTemplate.postForObject(startGameUrl,data,String.class);
    }

    //先判断是否能够匹配，然后进行匹配
    private boolean checkMatched(Player a, Player b){
        //段位差不超过等待时间 * 10
        int ratingDelta = Math.abs(a.getRating() - b.getRating());
        int waitTime = Math.min(a.getWaitingTime() ,b.getWaitingTime());
        return ratingDelta <= waitTime * 10;
    }

    private void matchingPlayers(){
        System.out.println("matching players : " + players.toString());
        //匹配过的玩家不进行匹配
        boolean[] used = new boolean[players.size()];
        //遍历所有玩家，进行匹配 这里从等待时间最长的玩家开始遍历
        for(int i = 0 ; i < players.size(); i ++ ){
            if(used[i]) continue;
            for(int j = i + 1 ; j < players.size(); j ++ ){
                if(used[j]) continue;
                Player a = players.get(i) , b = players.get(j);
                if(checkMatched(a , b)){
                    used[i] = used[j] = true;
                    sendResult(a, b);
                    break;
                }
            }
        }
        //将匹配成功的玩家删除掉
        List<Player> newPlayers = new ArrayList<>();

        for(int i = 0 ; i < players.size(); i ++ ){
            if(!used[i]){
                newPlayers.add(players.get(i));
            }
        }

        players = newPlayers;
    }

    private void increaseWaitingTime(){
        for(Player player : players){
            player.setWaitingTime(player.getWaitingTime() + 1);
        }
    }

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(1000);  //每次循环等待一秒 进行一次匹配
                lock.lock();
                try {
                    //使用到players所以加锁
                    increaseWaitingTime();
                    matchingPlayers();
                }finally {
                    lock.unlock();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
