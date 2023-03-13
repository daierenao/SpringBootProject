package com.kob.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/1/7 21:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @TableId(type = IdType.AUTO) //自增
    private Integer id;
    private String username;
    private String password;
    private String photo;
}
