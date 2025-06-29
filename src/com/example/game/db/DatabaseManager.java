package com.example.game.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:rinkko_game.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        String createPlayerTable = """
            CREATE TABLE IF NOT EXISTS player (
                id INTEGER PRIMARY KEY,
                money INTEGER NOT NULL
            )
            """;
        
        String createRinkkoTable = """
            CREATE TABLE IF NOT EXISTS rinkko (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                hunger INTEGER,
                thirst INTEGER,
                mood INTEGER,
                health INTEGER,
                affection INTEGER,
                player_id INTEGER,
                FOREIGN KEY (player_id) REFERENCES player(id)
            )
            """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createPlayerTable);
            stmt.execute(createRinkkoTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}