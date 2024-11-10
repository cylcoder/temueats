package com.sparta.temueats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class TemueatsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemueatsApplication.class, args);
    }

}
