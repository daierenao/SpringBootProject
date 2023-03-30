package com.kob.backend.service.record;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;

import java.util.List;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/27 17:35
 */
public interface GetRecordListService {
    JSONObject getRecordList(Integer page);
}
