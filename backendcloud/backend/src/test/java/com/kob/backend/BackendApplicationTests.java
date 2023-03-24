package com.kob.backend;

import com.kob.backend.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("2"));
        System.out.println(passwordEncoder.encode("3"));
        System.out.println(passwordEncoder.encode("adadda"));
        System.out.println(passwordEncoder.encode("pyxc"));
    }

}
