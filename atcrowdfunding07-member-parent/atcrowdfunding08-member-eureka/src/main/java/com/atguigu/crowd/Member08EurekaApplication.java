package com.atguigu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class Member08EurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(Member08EurekaApplication.class, args);
    }

}
