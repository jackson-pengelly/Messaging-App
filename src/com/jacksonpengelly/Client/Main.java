package com.jacksonpengelly.Client;
import com.jacksonpengelly.SharedConfig.ServerInformation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static com.jacksonpengelly.Client.Config.Commands.*;

public class Main {
    public static void start() {
        try (Socket socket = new Socket(ServerInformation.SERVER_IP, ServerInformation.GENERAL_PORT);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {
            // start thread to listen to the server
            Thread listenerThread = new Thread(() -> {
                try {
                    String messageFromServer;
                    while ((messageFromServer = reader.readLine()) != null) {
                        System.out.println(messageFromServer);
                    }
                } catch (IOException e) {
                    System.out.println("Connection lost");
                }
            });

            while (true) {
                String serverMsg = reader.readLine();
                if (serverMsg == null) break;

                System.out.println(serverMsg);

                if (serverMsg.contains("Login successful!") || serverMsg.contains("Account created!")) break;

                String answer = scanner.nextLine();
                writer.println(answer);
            }

            // start thread after getting username
            listenerThread.start();

            // keep main thread running to send messages
            //noinspection WhileCanBeDoWhile
            while (true) {
                String userInput = scanner.nextLine();
                switch (userInput) {
                    case HELP:
                        help();
                        break;
                    case EXIT:
                        break;
                }
            }
        } catch (IOException e) {
            System.err.println("Not connected to server. Possibly because server is not running.");
        }
    }

    public static void help() {
        System.out.println("\t\tHelp Menu");
        System.out.println("Help: /help (shows this menu)");
        System.out.println("Exit: /exit (disconnects you from the server and closes app)");
    }

    static void main() {
        Main.start();
    }
}