package com.jacksonpengelly.Server;

import com.jacksonpengelly.SharedConfig.ServerInformation;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.jacksonpengelly.Server.Config.Commands.*;

public class Main {
    public static final List<ClientHandler> activeClients = new CopyOnWriteArrayList<>();

    private static void start() {
        try (ServerSocket serverSocket = new ServerSocket(ServerInformation.GENERAL_PORT);
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("Server started successfully; listening on general port: " + ServerInformation.GENERAL_PORT);

            // main thread to listen for messages sent on server
            Thread listenerThread = new Thread(() -> {
               while (true) {
                   String input = scanner.nextLine();

                   // check for commands
                   switch (input) {
                       case SHUTDOWN:
                           broadcast("(Server): Server is shutting down in 3 seconds...");
                           try {
                               Thread.sleep(3);
                           } catch (InterruptedException e) {
                               System.err.println(e.getMessage());
                           }
                           System.exit(0);
                           break;
                       case HELP:
                           help();
                           break;
                       default:
                           broadcast("(Server): " + input);
                           break;
                   }
               }
            });
            listenerThread.start();

            while (true) {
                Socket client = serverSocket.accept();
                try {
                    ClientHandler handler = new ClientHandler(client);
                    activeClients.add(handler);
                    new Thread(handler).start();
                } catch (IOException e) {
                    System.err.println("Failed to initialize client:" + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Server Error: " + e.getMessage());
        }
    }

    private static void help() {
        System.out.println("/shutdown | Shutdowns the server for all users.");
        System.out.println("/help | Displays this menu.");
    }

    private static void broadcast(String message) {
        for (ClientHandler client : activeClients) {
            client.sendMessage(message);
        }
    }

    static void main() {
        start();
    }
}
