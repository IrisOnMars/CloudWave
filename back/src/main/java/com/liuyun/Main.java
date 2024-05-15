package com.liuyun;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @PackageName com.liuyun
 * @Description TODO
 * @Author Ren
 * @Date ${DATE} ${TIME}
 * @Version 1.0
 */
@SpringBootApplication
@MapperScan("com.liuyun.mapper")
public class Main {
    public static void main(String[] args) {

        SpringApplication.run(Main.class,args);
    }
}