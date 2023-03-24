package com.kob.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/1/7 22:07
 */
@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
