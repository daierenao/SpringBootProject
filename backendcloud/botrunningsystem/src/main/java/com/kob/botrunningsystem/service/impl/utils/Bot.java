package com.kob.botrunningsystem.service.impl.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/24 20:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bot {
    private Integer userId;
    private String botCode;
    private String input;
}
