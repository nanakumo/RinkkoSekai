package com.example.game.db;

public class SimpleTest {
    public static void main(String[] args) {
        System.out.println("Hello World - 测试开始");
        
        try {
            DatabaseManager.initializeDatabase();
            System.out.println("数据库初始化成功");
        } catch (Exception e) {
            System.out.println("错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}