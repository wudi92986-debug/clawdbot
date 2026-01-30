package com.petfuneral;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 宠物殡葬管理系统 - 启动类
 */
@SpringBootApplication
@MapperScan("com.petfuneral.mapper")
public class PetFuneralApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetFuneralApplication.class, args);
        System.out.println("====================================");
        System.out.println("  宠物殡葬管理系统启动成功！");
        System.out.println("  API文档: http://localhost:8080/swagger-ui.html");
        System.out.println("====================================");
    }
}
