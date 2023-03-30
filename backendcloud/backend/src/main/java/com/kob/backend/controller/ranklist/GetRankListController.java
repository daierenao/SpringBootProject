package com.kob.backend.controller.ranklist;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.pojo.User;
import com.kob.backend.service.ranklist.GetRankListService;
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
 * @date 2023/3/27 15:01
 */
@RestController
public class GetRankListController {

    @Autowired
    GetRankListService getRankListService;

    @GetMapping("/ranklist/getlist/")
    public JSONObject getRankList(@RequestParam Map<String,String> data){
        int page = Integer.parseInt(data.get("page"));
        return getRankListService.getList(page);
    }


}
