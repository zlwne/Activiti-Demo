package com.example.activity.demo;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 所有的流程操作代码及注释见junit
 */
@SpringBootApplication( exclude = SecurityAutoConfiguration.class)
@MapperScan(basePackages = {"com.example.activity.demo.mapper"})
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
