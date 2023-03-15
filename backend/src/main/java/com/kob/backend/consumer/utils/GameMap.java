package com.kob.backend.consumer.utils;

import com.alibaba.fastjson2.JSON;

import java.util.Random;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/15 18:52
 */
public class GameMap {
    private Integer rows;
    private Integer cols;
    private Integer inner_walls;
    private final int[][] g;
    private final static int []dx = {0,1,0,-1};
    private final static int []dy = {-1,0,1,0};
    public GameMap(Integer rows, Integer cols, Integer inner_walls) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls = inner_walls;
        this.g = new int[rows][cols];
    }

    public int[][] getG() {
        return g;
    }

    public boolean draw(){
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
    public boolean check_connectivity(int sx,int sy , int ex , int ey){
        if(sx == ex && sy == ey) return true;
        g[sx][sy] = 1;

        for(int i = 0 ; i < 4 ; i ++ ){
            int x = sx + dx[i],
                    y = sy + dy[i];
            if(x >= 0 && x <= this.rows && y >= 0 && y <= this.cols && g[x][y] == 0){
                if(this.check_connectivity(x,y,ex,ey)){
                    g[sx][sy] = 0; //恢复现场
                    return true;
                }
            }
        }

        g[sx][sy] = 0;
        return false;
    }
    public void createMap(){
        for(int i = 0; i < 1000 ; i ++ ){
            if(draw()){
                break;
            }
        }
    }
}
