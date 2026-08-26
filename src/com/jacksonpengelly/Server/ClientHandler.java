package com.jacksonpengelly.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private PrintWriter output;
    private BufferedReader clientInput;
    private String clientIP;
    private String clientUsername;
    private boolean muted;
    private boolean loggedIn;

    public ClientHandler(Socket socket) throws IOException {
        this.clientSocket = socket;
        this.clientIP = clientSocket.getInetAddress().getHostAddress();
        this.output = new PrintWriter(socket.getOutputStream(), true);
        this.clientInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    // getters
    public String getClientIP() {
        return this.clientIP;
    }

    public String getClientUsername() {
        return this.clientUsername;
    }

    @Override
    public void run() {
    }
}
