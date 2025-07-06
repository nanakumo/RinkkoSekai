package com.example.game;

public class SimpleTest {
    public static void main(String[] args) {
        System.out.println("测试开始...");
        
        // 测试数据库连接
        try {
            com.example.game.db.DatabaseManager.initializeDatabase();
            System.out.println("✅ 数据库初始化成功");
        } catch (Exception e) {
            System.out.println("❌ 数据库初始化失败: " + e.getMessage());
        }
        
        // 测试Player创建
        try {
            Player player = new Player();
            player.setId(1);
            player.setMoney(100);
            System.out.println("✅ 玩家创建成功，金币: " + player.getMoney());
        } catch (Exception e) {
            System.out.println("❌ 玩家创建失败: " + e.getMessage());
        }
        
        System.out.println("测试完成！");
    }
}