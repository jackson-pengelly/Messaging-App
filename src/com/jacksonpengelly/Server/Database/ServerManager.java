package com.jacksonpengelly.Server.Database;

import org.mindrot.jbcrypt.BCrypt;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class ServerManager {
    private static final Properties properties = new Properties();

    // static block to run once on class load
    static {
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("Could not load config.properties file.");
            System.err.println(e.getMessage());
        }
    }

    // connects to database using credentials from config.properties
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password")
        );
    }

    // creates a server in the servers table
    public static void createServer(String name, String plainTextPassword) {
        String sql = "INSERT INTO servers (server_name, is_public, password_hash) VALUES (?, ?, ?)";

        boolean isPublic = (plainTextPassword == null || plainTextPassword.trim().isEmpty());
        String finalHash = null;

        // if server is private hash password with bcrypt
        if (!isPublic) {
            finalHash = BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
        }

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setBoolean(2, isPublic);

            if (finalHash != null) {
                pstmt.setString(3, finalHash);
            } else {
                pstmt.setNull(3, java.sql.Types.CHAR);
            }

            pstmt.executeUpdate();
            System.out.println("Successfully saved server: " + name);
        } catch (SQLException e) {
            System.err.println("Error saving server to database.");
            System.err.println(e.getMessage());
        }
    }
}