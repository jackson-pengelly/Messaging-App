package com.jacksonpengelly.Server.Database;

import com.jacksonpengelly.Server.DAOs.ServerDAO;
import com.jacksonpengelly.Server.Objects.Server;

public class ServerManager {
    private static final ServerDAO serverDAO = new ServerDAO();

    public static void requestNewServerCreation(String name, String ip, String password) {
        if (serverDAO.existsByName(name)) {
            System.out.println("Creation failed: A server named '" + name + "' already exists.");
            return;
        }

        boolean isPublic = (password == null || password.trim().isEmpty());
        String hashedPassword = PasswordManagement.hashPassword(password);

        Server server = new Server(name, ip, isPublic, hashedPassword);

        serverDAO.createServer(server);
        System.out.println("Server created.");
    }
}