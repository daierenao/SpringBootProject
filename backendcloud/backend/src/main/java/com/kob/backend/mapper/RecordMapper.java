package com.kob.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.pojo.Record;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/21 23:29
 */
@Repository
@Mapper
public interface RecordMapper extends BaseMapper<Record> {
}
