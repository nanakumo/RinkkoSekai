package com.example.game.db;

import com.example.game.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDAO {

    public Player findById(int id) {
        String sql = "SELECT * FROM player WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Player player = new Player();
                    player.setId(rs.getInt("id"));
                    player.setMoney(rs.getInt("money"));
                    return player;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save(Player player) {
        String updateSql = "UPDATE player SET money = ? WHERE id = ?";
        String insertSql = "INSERT INTO player (id, money) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnection()) {
            // First, try to update the player.
            try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                pstmt.setInt(1, player.getMoney());
                pstmt.setInt(2, player.getId());
                int affectedRows = pstmt.executeUpdate();

                // If no rows were affected, it means the player doesn't exist. So, insert.
                if (affectedRows == 0) {
                    try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
                        insertPstmt.setInt(1, player.getId());
                        insertPstmt.setInt(2, player.getMoney());
                        insertPstmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
