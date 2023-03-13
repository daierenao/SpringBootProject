package com.kob.backend.service.user.account;

import java.util.Map;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/9 22:46
 */
public interface LoginService {
    Map<String,String> getToken(String username, String password);
}
