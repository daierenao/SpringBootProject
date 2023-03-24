package com.kob.backend.consumer.utils;

import com.kob.backend.utils.JwtUtil;
import io.jsonwebtoken.Claims;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description 将token中的userId解析出来
 * @date 2023/3/15 14:07
 */
public class JwtAuthentication {
    public static Integer getUserId(String token) {
        int userid = -1;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            throw new RuntimeException(e);
       }
        return userid;
    }
}
