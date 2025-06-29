package com.example.game.db;

import java.util.List;
import com.example.pet.Rinkko;

public class RinkkoDAOTest {
    
    public static void main(String[] args) {
        System.out.println("=== RinkkoDAO 测试开始 ===");
        
        // 1. 初始化数据库
        testDatabaseInitialization();
        
        // 2. 测试保存功能
        testSave();
        
        // 3. 测试查询功能
        testFindByPlayerId();
        
        // 4. 测试更新功能
        testUpdate();
        
        System.out.println("=== RinkkoDAO 测试完成 ===");
    }
    
    private static void testDatabaseInitialization() {
        System.out.println("\n1. 测试数据库初始化...");
        try {
            DatabaseManager.initializeDatabase();
            System.out.println("✅ 数据库初始化成功");
        } catch (Exception e) {
            System.out.println("❌ 数据库初始化失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testSave() {
        System.out.println("\n2. 测试保存功能...");
        try {
            RinkkoDAO dao = new RinkkoDAO();
            
            // 创建测试宠物
            Rinkko testPet = new Rinkko();
            testPet.setName("测试凛喵喵");
            
            System.out.println("保存前 - ID: " + testPet.getId());
            System.out.println("保存前 - 状态: " + testPet.getStatus());
            
            // 保存到数据库
            dao.save(testPet, 1); // 玩家ID为1
            
            System.out.println("保存后 - ID: " + testPet.getId());
            System.out.println("✅ 保存功能测试成功");
        } catch (Exception e) {
            System.out.println("❌ 保存功能测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testFindByPlayerId() {
        System.out.println("\n3. 测试查询功能...");
        try {
            RinkkoDAO dao = new RinkkoDAO();
            
            // 查询玩家1的所有宠物
            List<Rinkko> pets = dao.findByPlayerId(1);
            
            System.out.println("查询到 " + pets.size() + " 只宠物:");
            for (int i = 0; i < pets.size(); i++) {
                Rinkko pet = pets.get(i);
                System.out.println("  宠物 " + (i+1) + ":");
                System.out.println("    ID: " + pet.getId());
                System.out.println("    名称: " + pet.getName());
                System.out.println("    饱食度: " + pet.getHunger());
                System.out.println("    口渴度: " + pet.getThirst());
                System.out.println("    心情: " + pet.getMood());
                System.out.println("    健康: " + pet.getHealth());
                System.out.println("    好感: " + pet.getAffection());
            }
            System.out.println("✅ 查询功能测试成功");
        } catch (Exception e) {
            System.out.println("❌ 查询功能测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testUpdate() {
        System.out.println("\n4. 测试更新功能...");
        try {
            RinkkoDAO dao = new RinkkoDAO();
            
            // 先查询一只宠物
            List<Rinkko> pets = dao.findByPlayerId(1);
            if (pets.isEmpty()) {
                System.out.println("⚠️ 没有找到宠物，跳过更新测试");
                return;
            }
            
            Rinkko pet = pets.get(0);
            System.out.println("更新前 - 名称: " + pet.getName());
            
            // 修改宠物名称
            pet.setName("更新后的凛喵喵");
            
            // 模拟状态变化（通过changeMood等方法）
            pet.changeMood(10);
            
            System.out.println("更新前 - 心情: " + (pet.getMood() - 10));
            System.out.println("更新后 - 心情: " + pet.getMood());
            
            // 保存更新
            dao.update(pet);
            
            // 重新查询验证
            Rinkko updatedPet = dao.findById(pet.getId());
            if (updatedPet != null) {
                System.out.println("验证更新 - 名称: " + updatedPet.getName());
                System.out.println("验证更新 - 心情: " + updatedPet.getMood());
                System.out.println("✅ 更新功能测试成功");
            } else {
                System.out.println("❌ 更新后无法找到宠物");
            }
        } catch (Exception e) {
            System.out.println("❌ 更新功能测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}