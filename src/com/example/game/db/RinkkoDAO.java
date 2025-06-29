package com.example.game.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.example.pet.Rinkko;

public class RinkkoDAO {

    public void save(Rinkko pet, int playerId) {
        String sql = "INSERT INTO rinkko (name, hunger, thirst, mood, health, affection, player_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, pet.getName());
            pstmt.setInt(2, pet.getHunger());
            pstmt.setInt(3, pet.getThirst());
            pstmt.setInt(4, pet.getMood());
            pstmt.setInt(5, pet.getHealth());
            pstmt.setInt(6, pet.getAffection());
            pstmt.setInt(7, playerId);
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pet.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Rinkko pet) {
        String sql = "UPDATE rinkko SET name = ?, hunger = ?, thirst = ?, mood = ?, health = ?, affection = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, pet.getName());
            pstmt.setInt(2, pet.getHunger());
            pstmt.setInt(3, pet.getThirst());
            pstmt.setInt(4, pet.getMood());
            pstmt.setInt(5, pet.getHealth());
            pstmt.setInt(6, pet.getAffection());
            pstmt.setInt(7, pet.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Rinkko findById(int id) {
        String sql = "SELECT * FROM rinkko WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createRinkkoFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Rinkko> findByPlayerId(int playerId) {
        List<Rinkko> pets = new ArrayList<>();
        String sql = "SELECT * FROM rinkko WHERE player_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, playerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    pets.add(createRinkkoFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pets;
    }

    private Rinkko createRinkkoFromResultSet(ResultSet rs) throws SQLException {
        Rinkko pet = new Rinkko();
        pet.setId(rs.getInt("id"));
        pet.setName(rs.getString("name"));
        
        // 通过反射或其他方式设置私有字段，这里暂时使用一个辅助方法
        setRinkkoStats(pet, 
            rs.getInt("hunger"), 
            rs.getInt("thirst"), 
            rs.getInt("mood"), 
            rs.getInt("health"), 
            rs.getInt("affection"));
        
        return pet;
    }

    private void setRinkkoStats(Rinkko pet, int hunger, int thirst, int mood, int health, int affection) {
        // 由于Rinkko类的状态字段是private，我们需要通过其方法来设置
        // 这是一个临时方案，理想情况下应该在Rinkko类中添加用于数据库加载的构造函数或setter方法
        
        // 计算需要调整的差值并应用
        int hungerDiff = hunger - pet.getHunger();
        int thirstDiff = thirst - pet.getThirst();
        int moodDiff = mood - pet.getMood();
        int healthDiff = health - pet.getHealth();
        int affectionDiff = affection - pet.getAffection();
        
        // 注意：这些方法在Rinkko类中是protected，需要在同一包中或创建public方法
        // 这里假设我们会修改Rinkko类来支持数据库加载
        try {
            java.lang.reflect.Method changeHunger = pet.getClass().getDeclaredMethod("changeHunger", int.class);
            java.lang.reflect.Method changeThirst = pet.getClass().getDeclaredMethod("changeThirst", int.class);
            java.lang.reflect.Method changeMood = pet.getClass().getDeclaredMethod("changeMood", int.class);
            java.lang.reflect.Method changeHealth = pet.getClass().getDeclaredMethod("changeHealth", int.class);
            java.lang.reflect.Method changeAffection = pet.getClass().getDeclaredMethod("changeAffection", int.class);
            
            changeHunger.setAccessible(true);
            changeThirst.setAccessible(true);
            changeMood.setAccessible(true);
            changeHealth.setAccessible(true);
            changeAffection.setAccessible(true);
            
            changeHunger.invoke(pet, hungerDiff);
            changeThirst.invoke(pet, thirstDiff);
            changeMood.invoke(pet, moodDiff);
            changeHealth.invoke(pet, healthDiff);
            changeAffection.invoke(pet, affectionDiff);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}