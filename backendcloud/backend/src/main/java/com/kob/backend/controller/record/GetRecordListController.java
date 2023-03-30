package com.kob.backend.controller.record;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.pojo.Record;
import com.kob.backend.service.impl.pk.ReceiveBotMoveServiceImpl;
import com.kob.backend.service.impl.record.GetRecordListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/27 17:41
 */
@RestController
public class GetRecordListController {

    @Autowired
    GetRecordListServiceImpl recordListService;

    @GetMapping("/record/getlist/")
    public JSONObject getRecordList(@RequestParam Map<String,String> data){
        int page = Integer.parseInt(data.get("page"));
        return recordListService.getRecordList(page);
    }

}
