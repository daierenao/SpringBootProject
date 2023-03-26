package com.kob.botrunningsystem.service.utils;


import java.util.ArrayList;
import java.util.List;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description bot代码测试
 * @date 2023/3/24 22:42
 */
public class Bot implements BotInterface{

    static class Cell{
        public int x , y;
        public Cell(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    //判断当前步数是否需要自增长度
    private boolean check_tail_increasing(int steps) {
        if (steps <= 10) return true;
        return steps % 3 == 1;
    }

    public List<Cell> getCells(int sx , int sy , String steps){
        List<Cell> cells = new ArrayList<>();
        steps = steps.substring(1,steps.length() - 1);
        int []dx = {-1 , 0 , 1 , 0} , dy = {0 , 1 , 0 , - 1};
        int x = sx , y = sy;
        int step = 0;
        cells.add(new Cell(x,y));
        //根据走过的步数生成蛇的身体
        for (int i = 0 ; i < steps.length(); i ++) {
            int d = steps.charAt(i) - '0';
            x += dx[d];
            y += dy[d];
            cells.add(new Cell(x,y));
            if(!check_tail_increasing( ++ step))
                cells.remove(0); // 如果不是需要增加蛇长度的移动步数，则删除蛇尾
        }
        return cells;
    }


    @Override
    public Integer nextMove(String input) {
        String[] strs = input.split("#");
        int [][] g = new int[13][14];
        //地图初始化
        for (int i = 0 , k = 0; i < 13; i++) {
            for (int j = 0; j < 14; j++ , k ++) {
                if(strs[0].charAt(k) == '1')
                g[i][j] = 1;
            }
        }

        int mySx = Integer.parseInt(strs[1]) , mySy = Integer.parseInt(strs[2]);
        int opSx = Integer.parseInt(strs[4]) , opSy = Integer.parseInt(strs[5]);

        List<Cell> myCells = getCells(mySx,mySy,strs[3]);
        List<Cell> opCells = getCells(opSx,opSy,strs[6]);

        //不能移动到自己和对手的位置
        for(Cell cell : myCells) g[cell.x][cell.y] = 1;
        for(Cell cell : opCells) g[cell.x][cell.y] = 1;

        int []dx = {-1 , 0 , 1 , 0} , dy = {0 , 1 , 0 , - 1};
        for(int i = 0 ; i < 4 ; i ++ ){
            int x = myCells.get(myCells.size() - 1).x + dx[i];
            int y = myCells.get(myCells.size() - 1).y + dy[i];
            if(x >= 0 && x < 13 && y >=0 && y < 14 && g[x][y] == 0){
                return i;
            }
        }
        return 0;
    }
}

