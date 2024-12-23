package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class AppConfigTest {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void testPasswordEncoder() {
        Assertions.assertNotNull(passwordEncoder);
        System.out.println(passwordEncoder.encode("test"));
    }
}
