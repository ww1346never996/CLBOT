package com.example.clbot;

import com.example.clbot.controller.BotMain;
import com.example.clbot.service.IBotListService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.clbot.mapper")
@SpringBootApplication
public class ClbotApplication implements CommandLineRunner {

    @Autowired
    IBotListService service;
    @Autowired
    BotMain botMain;

    public static void main(String[] args) {
        SpringApplication.run(ClbotApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        botMain.run();
    }
}
