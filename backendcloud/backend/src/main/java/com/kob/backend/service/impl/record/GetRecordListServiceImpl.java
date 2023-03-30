package com.kob.backend.service.impl.record;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.record.GetRecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/27 17:36
 */
@Service
public class GetRecordListServiceImpl implements GetRecordListService {

    @Autowired
    RecordMapper recordMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public JSONObject getRecordList(Integer page) {
        //第一个参数是当前页数， 第二个参数是展示的行数
        IPage<Record> recordIPage = new Page<>(page,10);
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");

        List<Record> records = recordMapper.selectPage(recordIPage,queryWrapper).getRecords();

        JSONObject resp = new JSONObject();

        List<JSONObject> items = new LinkedList<>();

        for(Record record : records){
            User a = userMapper.selectById(record.getAId());
            User b = userMapper.selectById(record.getBId());

            JSONObject item = new JSONObject();
            //对局双信息
            item.put("a_username",a.getUsername());
            item.put("a_photo",a.getPhoto());
            item.put("b_username",b.getUsername());
            item.put("b_photo",b.getPhoto());
            String result = "平局";
            if("A".equals(record.getLoser()))
                result = "B胜";
            else if("B".equals(record.getLoser()))
                result = "A胜";
            item.put("result",result);
            item.put("record",record);

            items.add(item);
        }

        resp.put("records",items);
        resp.put("records_count",recordMapper.selectCount(null));
        return resp;
    }
}
