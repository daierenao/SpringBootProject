package com.kob.backend.consumer.utils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.omg.CORBA.INTERNAL;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/17 19:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer id;
    private Integer sx;
    private Integer sy;
    private List<Integer> steps; //走过的路径

    //判断当前步数是否需要自增长度
    private boolean check_tail_increasing(int steps) {
        if (steps <= 10) return true;
        return steps % 3 == 1;
    }

    public List<Cell> getCells(){
        List<Cell> cells = new ArrayList<>();
        int []dx = {-1 , 0 , 1 , 0} , dy = {0 , 1 , 0 , - 1};
        int x = sx , y = sy;
        int step = 0;
        cells.add(new Cell(x,y));
        //根据走过的步数生成蛇的身体
        for (int d : steps) {
            x += dx[d];
            y += dy[d];
            cells.add(new Cell(x,y));
            if(!check_tail_increasing( ++ step))
                cells.remove(0); // 如果不是需要增加蛇长度的移动步数，则删除蛇尾
        }
        return cells;
    }

    public String getStepsString(){
        StringBuilder stepsString = new StringBuilder();
        for (int step : steps) {
            stepsString.append(step);
        }
        return stepsString.toString();
    }
}
