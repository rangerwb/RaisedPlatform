package com.atguigu.crowd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.atguigu.crowd.mapper")
@SpringBootApplication
public class Atcrowdfunding10MemberMysqlProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(Atcrowdfunding10MemberMysqlProviderApplication.class, args);
    }

}
