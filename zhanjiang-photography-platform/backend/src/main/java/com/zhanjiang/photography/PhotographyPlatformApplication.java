package com.zhanjiang.photography;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 湛江高校大学生约拍平台启动类
 */
@SpringBootApplication
@MapperScan("com.zhanjiang.photography.mapper")
@EnableAsync
@EnableScheduling
public class PhotographyPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhotographyPlatformApplication.class, args);
        System.out.println("========================================");
        System.out.println("  湛江高校大学生约拍平台启动成功！");
        System.out.println("  API文档地址: http://localhost:8080/api/v1/doc.html");
        System.out.println("========================================");
    }
}
