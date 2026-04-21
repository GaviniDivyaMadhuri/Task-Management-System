package com.taskflow.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:taskflow.db";
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
        }
        return connection;
    }

    public static void initialize() {
        String sql = """
                CREATE TABLE IF NOT EXISTS tasks (
                    id          INTEGER PRIMARY KEY AUTOINCREMENT,
                    title       TEXT    NOT NULL,
                    description TEXT,
                    priority    TEXT    NOT NULL DEFAULT 'MEDIUM',
                    status      TEXT    NOT NULL DEFAULT 'PENDING',
                    category    TEXT,
                    due_date    TEXT,
                    created_date TEXT
                );
                """;
        try (Statement stmt = getConnection().createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
