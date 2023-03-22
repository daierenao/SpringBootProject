package com.kob.backend.consumer.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.Record;
import org.springframework.security.core.parameters.P;
import sun.applet.AppletResourceLoader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/15 18:52
 */
public class GameMap extends Thread{
    private Integer rows;
    private Integer cols;
    private Integer inner_walls;
    private final int[][] g;
    private final static int[] dx = {-1,0 ,1 ,0};
    private final static int[] dy = {0 , 1 , 0 , - 1};
    private final Player PlayerA, PlayerB;
    private Integer nextStepA = null;
    private Integer nextStepB = null;
    private ReentrantLock lock = new ReentrantLock();
    private String status = "playing"; //playing表示游戏正在进行 , finished表示游戏结束
    private String loser = "all" ; //all表示平局 A：A输 B：B输
    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        }
        finally {
            lock.unlock();
        }

    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try{
            this.nextStepB = nextStepB;
        }
        finally {
            lock.unlock();
        }
    }


    public GameMap(Integer rows, Integer cols, Integer inner_walls, Integer idA, Integer idB) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls = inner_walls;
        this.g = new int[rows][cols];
        this.PlayerA = new Player(idA, this.rows - 2, 1, new ArrayList<>());
        this.PlayerB = new Player(idB, 1, this.cols - 2, new ArrayList<>());
    }

    public Player getPlayerA() {
        return PlayerA;
    }

    public Player getPlayerB() {
        return PlayerB;
    }

    public int[][] getG() {
        return g;
    }

    public boolean draw() {
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.cols; c++) {
                g[r][c] = 0;
            }
        }
        //周围的墙
        for (int r = 0; r < this.rows; r++) {
            g[r][0] = g[r][this.cols - 1] = 1;
        }
        //周围的墙
        for (int c = 0; c < this.cols; c++) {
            g[0][c] = g[this.rows - 1][c] = 1;
        }
        //随机生成内部的障碍物
        Random random = new Random();
        for (int i = 0; i < this.inner_walls / 2; i++) {
            for (int j = 0; j < 1000; j++) {
                //生成(0 ~ 13)随机行数
                int r = random.nextInt(this.rows);
                //生成随机列数
                int c = random.nextInt(this.cols);
                //如果该位置已经生成了障碍物则跳过
                if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1) continue;
                //不能将两个玩家的起点设为障碍
                if ((r == this.rows - 2 && c == 1) || (c == this.cols - 2 && r == 1))
                    continue;
                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }
        return this.check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    public boolean check_connectivity(int sx, int sy, int ex, int ey) {
        if (sx == ex && sy == ey) return true;
        g[sx][sy] = 1;

        for (int i = 0; i < 4; i++) {
            int x = sx + dx[i],
                    y = sy + dy[i];
            if (x >= 0 && x <= this.rows && y >= 0 && y <= this.cols && g[x][y] == 0) {
                if (this.check_connectivity(x, y, ex, ey)) {
                    g[sx][sy] = 0; //恢复现场
                    return true;
                }
            }
        }

        g[sx][sy] = 0;
        return false;
    }

    public void createMap() {
        for (int i = 0; i < 1000; i++) {
            if (draw()) {
                break;
            }
        }
    }

    public boolean nextStep(){ //等待两名玩家的下一步操作 , 这里设置如果双方五秒内没有给出操作 则判断输赢
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(100);
                lock.lock();
                try{
                    //如果在该时刻 双方都输入则进行下一步
                    if(nextStepA != null && nextStepB != null) {
                        PlayerA.getSteps().add(nextStepA);
                        PlayerB.getSteps().add(nextStepB);
                        return true;
                    }
                }finally {
                    lock.unlock();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    //判断这一步是否合法
    private boolean check_valid(List<Cell> cellsA , List<Cell> cellsB){
        int n = cellsA.size();
        Cell cell = cellsA.get(n - 1);
        //如果撞墙则非法
        if(g[cell.getX()][cell.getY()] == 1) return false;

        //判断是否撞自己 除蛇头外
        for(int i = 0 ; i < n - 1 ; i ++ ){
            if(cellsA.get(i).x == cell.x && cellsA.get(i).y == cell.y)
                return false;
        }
        //判断是否撞另一条蛇 除蛇头外
        for(int i = 0 ; i < n - 1 ; i ++ ){
            if(cellsB.get(i).x == cell.x && cellsB.get(i).y == cell.y)
                return false;
        }
        return true;
    }

    private void judge(){  //判断两名玩家的下一步操作是否合法
        List<Cell> cellsA = PlayerA.getCells();
        List<Cell> cellsB = PlayerB.getCells();
        //判断两条蛇是否撞墙，以及是否撞到另一条蛇的身体
        boolean validA = check_valid(cellsA,cellsB); //传入该蛇 以及另一条蛇 判断是否撞墙撞自己或撞另一条蛇
        boolean validB = check_valid(cellsB,cellsA);

        if(!validA || !validB){
            status = "finished";
            if(!validA && !validB){
                loser = "all";
            }
            else if (!validA){
                loser = "A";
            }
            else {
                loser = "B";
            }
        }

    }
    private void sendAllMessage(String message){ //向两个Client发送信息
        WebSocketServer.users.get(PlayerA.getId()).sendMessage(message);
        WebSocketServer.users.get(PlayerB.getId()).sendMessage(message);
    }
    private void sendMove(){ //向两个Client发送移动信息
        JSONObject resp = new JSONObject();
        lock.lock();
        try{
            resp.put("event","move");
            resp.put("a_direction",nextStepA);
            resp.put("b_direction",nextStepB);
            sendAllMessage(resp.toJSONString());
            nextStepA = nextStepB = null; //进行下一步之前清空
        }finally {
            lock.unlock();
        }

    }

    private String getMapString(){
        StringBuilder map = new StringBuilder();
        for(int i = 0 ; i < rows ; i ++ ){
            for (int j = 0; j < cols; j++) {
                map.append(g[i][j]);
            }
        }
        return map.toString();
    }

    private void saveToDataBase(){
        Record record = new Record(
                null,
                PlayerA.getId(),
                PlayerA.getSx(),
                PlayerA.getSy(),
                PlayerB.getId(),
                PlayerB.getSx(),
                PlayerB.getSy(),
                PlayerA.getStepsString(),
                PlayerB.getStepsString(),
                getMapString(),
                loser,
                new Date()
        );
        WebSocketServer.recordMapper.insert(record);
    }

    private void sendResult(){ //向两个Client放回结果
        JSONObject resp = new JSONObject();
        resp.put("event","result");
        resp.put("loser",loser);
        if(nextStepA != null)
        resp.put("a_direction",nextStepA);
        if(nextStepB != null)  // 传入临死前的移动位置 用来修改眼睛位置
        resp.put("b_direction",nextStepB);
        saveToDataBase();
        sendAllMessage(resp.toJSONString());
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            if(nextStep()){ // 是否获取了两条蛇的下一步操作
                judge();
                if(status.equals("playing")){
                    sendMove();
                }
                else {
                    sendResult();
                    break;
                }
            }else {
                lock.lock();  //边界，当等待时间结束的边界时读取输入无效
                try {
                    if(nextStepA == null && nextStepB == null){
                        loser = "all";
                    }
                    else if (nextStepA == null){
                        loser = "A";
                    }
                    else loser = "B";
                }finally {
                    lock.unlock();
                }
                sendResult();
               break;
            }
        }
    }
}
