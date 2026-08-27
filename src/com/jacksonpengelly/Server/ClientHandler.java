package com.jacksonpengelly.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

import static com.jacksonpengelly.Server.Config.Colors.*;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final PrintWriter output;
    private final BufferedReader clientInput;
    private final String clientIP;
    private String clientUsername;
    private final String usernameColor;
    private boolean shouldBroadcastTo;

    public ClientHandler(Socket socket) throws IOException {
        this.clientSocket = socket;
        this.clientIP = clientSocket.getInetAddress().getHostAddress();
        this.output = new PrintWriter(socket.getOutputStream(), true);
        this.clientInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.shouldBroadcastTo = false;

        Random random = new Random();
        this.usernameColor = COLORS[random.nextInt(COLORS.length)];
    }

    public boolean shouldBroadcastTo() {
        return shouldBroadcastTo;
    }

    public void sendMessage(String message){
        output.println(message);
    }

    private void broadcast(String message) {
        for (ClientHandler client : Main.activeClients) {
            if (client != this && client.shouldBroadcastTo()) {
                client.sendMessage(message);
            }
        }
    }

    @Override
    public void run() {
        try {
            output.println("(Server): Please enter your username to join the chat: ");
            this.clientUsername = clientInput.readLine();
            if (this.clientUsername == null || this.clientUsername.trim().isEmpty()) {
                this.clientUsername = "Anonymous_" + clientSocket.getPort();
            }

            shouldBroadcastTo = true;
            output.println("(Server): Connection successful! You are now in the chatroom.");
            broadcast("(Server): " + clientUsername + " has joined the room.");
            System.out.println(clientUsername + " joined from IP: " + clientIP);

            String clientMessage;
            while ((clientMessage = clientInput.readLine()) != null) {
                broadcast(usernameColor + "(" + clientUsername + "): " + RESET + clientMessage);
            }
        } catch (IOException e) {
            System.out.println(clientUsername + " left the server.");
        } finally {
            cleanup();
        }
    }

    private void cleanup() {
        Main.activeClients.remove(this);
        if (clientUsername != null) {
            broadcast("(Server): " + clientUsername + " has disconnected.");
        }

        try {
            if (clientInput != null) clientInput.close();
            if (output != null) output.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}
