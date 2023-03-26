package com.kob.backend.service.impl.pk;


import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.consumer.utils.GameMap;
import com.kob.backend.service.pk.ReceiveBotMoveService;
import org.springframework.stereotype.Service;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/24 22:48
 */
@Service
public class ReceiveBotMoveServiceImpl implements ReceiveBotMoveService {

    private Integer direction_mapping(Integer d){
        if (d == 0) d = 2;
        else if (d == 1) d = 3;
        else if (d == 2) d = 0;
        else if (d == 3) d = 1;
        return d;
    };

    @Override
    public String receiveBotMove(Integer userId, Integer direction) {
        System.out.println("receive bot move: " + userId + " " + direction);
        if (WebSocketServer.users.get(userId) != null) {
            GameMap gameMap = WebSocketServer.users.get(userId).gameMap;
            if (gameMap != null) {
                if (gameMap.getPlayerA().getId().equals(userId)) {
                    gameMap.setNextStepA(direction);
                } else if (gameMap.getPlayerB().getId().equals(userId)) {
                    //直接根据后端地图进行下一步的判定 所以不需要地图映射
                        gameMap.setNextStepB(direction);
                }
            }
        }
        return "receive move successfully";
    }
}
