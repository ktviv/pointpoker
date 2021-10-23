package com.ktviv.pointpoker.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ktviv.pointpoker.app", "com.ktviv.pointpoker.domain", "com.ktviv.pointpoker.infra"})
public class PointPokerApplication {

    /**
     * The Application launcher main class
     *
     * @param args String[]
     */
    public static void main(String[] args) {
        SpringApplication.run(PointPokerApplication.class, args);
    }
}
