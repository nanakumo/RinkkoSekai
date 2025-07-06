package com.example.game;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;

@SpringBootApplication(exclude = {
    ValidationAutoConfiguration.class,
    SecurityAutoConfiguration.class,
    SpringApplicationAdminJmxAutoConfiguration.class,
    JmxAutoConfiguration.class
})
public class RinkkoWebApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(RinkkoWebApplication.class);
        
        // 添加优雅关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n🐱 凛喵喵世界正在安全关闭...");
            System.out.println("感谢使用凛喵喵世界！");
        }));
        
        app.run(args);
    }
}