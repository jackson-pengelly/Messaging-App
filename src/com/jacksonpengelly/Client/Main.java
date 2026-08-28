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
    private static void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter the server IP: ");
            String IP = scanner.nextLine();

            try (Socket socket = new Socket(IP, ServerInformation.GENERAL_PORT);
                 PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                // start thread to listen to the server
                Thread listenerThread = new Thread(() -> {
                    try {
                        String serverMsg;
                        while ((serverMsg = reader.readLine()) != null) {
                            System.out.println(serverMsg);
                        }
                    } catch (IOException e) {
                        System.out.println("Connection lost");
                    }
                });
                listenerThread.start();

                // keep main thread running to send messages
                while (true) {
                    String userInput = scanner.nextLine();
                    switch (userInput) {
                        case HELP:
                            help();
                            break;
                        case EXIT:
                            System.out.println("Disconnecting from the chatroom.");
                            System.exit(0);
                            break;
                        default:
                            writer.println(userInput);
                            break;
                    }
                }
            } catch (IOException e) {
                System.err.println("Failed to connect to server with IP: " + IP);
            }
        }
    }

    private static void help() {
        System.out.println("/exit | Disconnects you from the active room.");
        System.out.println("/help | Displays this help menu.");
    }

    public static void main(String[] args) {
        start();
    }
}