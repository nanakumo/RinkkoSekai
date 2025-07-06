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
        return new Rinkko(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getInt("hunger"),
            rs.getInt("thirst"),
            rs.getInt("mood"),
            rs.getInt("health"),
            rs.getInt("affection")
        );
    }
    
    // Web版本兼容方法
    public List<Rinkko> getAllRinkkosByPlayerId(int playerId) {
        return findByPlayerId(playerId);
    }
    
    public Rinkko getRinkkoById(int id) {
        return findById(id);
    }
    
    public void saveRinkko(Rinkko pet) {
        save(pet, pet.getPlayerId());
    }
    
    public void updateRinkko(Rinkko pet) {
        update(pet);
    }
}