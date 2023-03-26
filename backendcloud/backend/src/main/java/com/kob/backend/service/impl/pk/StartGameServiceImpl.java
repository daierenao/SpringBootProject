package com.kob.backend.service.impl.pk;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.service.pk.StartGameService;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/23 21:26
 */
@Service
public class StartGameServiceImpl implements StartGameService {
    @Override
    public String startGame(Integer aId,Integer aBotId , Integer bId , Integer bBotId) {
        WebSocketServer.startGame(aId,aBotId,bId, bBotId);
        return "start game successfully";
    }
}
