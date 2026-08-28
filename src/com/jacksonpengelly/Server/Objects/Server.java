package com.jacksonpengelly.Server.Objects;

import java.time.LocalDateTime;

public class Server {
    private int id;
    private String serverName;
    private boolean isPublic;
    private String passwordHash;
    private LocalDateTime createdAt;

    public Server(String serverName, boolean isPublic, String passwordHash) {
        this.serverName = serverName;
        this.isPublic = isPublic;
        this.passwordHash = passwordHash;
    }
}
