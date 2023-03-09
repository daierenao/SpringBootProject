package com.kob.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/1/7 22:07
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
