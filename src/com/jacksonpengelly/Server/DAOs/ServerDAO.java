package com.jacksonpengelly.Server.DAOs;

import com.jacksonpengelly.Server.Database.DatabaseConfig;
import com.jacksonpengelly.Server.Objects.Server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServerDAO {
    public boolean existsByName(String serverName) {
        String sql = "SELECT COUNT(*) FROM servers WHERE LOWER(server_name) = LOWER(?)";

        try (Connection conn = DatabaseConfig.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, serverName.trim());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // returns true if the server exists
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public void createServer(Server server) {
        String sql = "INSERT INTO servers (server_name, server_ip, is_public, password_hash) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, server.getServerName());
            pstmt.setString(2, server.getServerName());
            pstmt.setBoolean(3, server.isPublic());

            if (server.getPasswordHash() != null) {
                pstmt.setString(4, server.getPasswordHash());
            } else {
                pstmt.setNull(4, java.sql.Types.CHAR);
            }

            pstmt.executeUpdate();
            System.out.println("Successfully saved server: " + server.getServerName() + ".");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public Server findById(int id) {
        // TODO implement server searches by id
        return null;
    }

    public List<Server> findAllPublicServers() {
        // TODO implement later for showing a browser of public servers
        return new ArrayList<>();
    }
}
