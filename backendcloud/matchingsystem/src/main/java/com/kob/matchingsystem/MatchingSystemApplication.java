package com.kob.matchingsystem;

import com.kob.matchingsystem.service.impl.MatchingServiceImpl;
import com.kob.matchingsystem.service.impl.utils.MatchingPool;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/22 22:59
 */
@SpringBootApplication
public class MatchingSystemApplication {
    public static void main(String[] args) {
        MatchingServiceImpl.matchingpool.start(); //启动匹配线程
        SpringApplication.run(MatchingSystemApplication.class,args);
    }
}
