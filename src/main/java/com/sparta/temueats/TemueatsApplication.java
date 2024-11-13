package com.sparta.temueats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableJpaAuditing
@EnableWebMvc
@EnableScheduling
public class TemueatsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemueatsApplication.class, args);
    }

}
