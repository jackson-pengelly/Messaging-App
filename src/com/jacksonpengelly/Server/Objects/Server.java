package com.jacksonpengelly.Server.Objects;

import java.time.LocalDateTime;

public class Server {
    private int id;
    private String serverName;
    private String serverIP;
    private boolean isPublic;
    private String passwordHash;
    private LocalDateTime createdAt;

    public Server(String serverName, String serverIP, boolean isPublic, String passwordHash) {
        this.serverName = serverName;
        this.serverIP = serverIP;
        this.isPublic = isPublic;
        this.passwordHash = passwordHash;
    }

    public String getServerName() {
        return serverName;
    }

    public String getServerIP() {
        return serverIP;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean isPublic() {
        return isPublic;
    }
}
