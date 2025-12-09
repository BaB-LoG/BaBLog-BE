package com.ssafy.bablog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.ssafy.bablog.member.repository.mapper")
@EnableScheduling
public class BaBLogBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaBLogBeApplication.class, args);
    }

}
