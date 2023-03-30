package com.kob.backend.service.ranklist;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.pojo.User;

import java.util.List;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/27 14:59
 */
public interface GetRankListService {
    JSONObject getList(Integer page);
}
