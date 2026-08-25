package com.jacksonpengelly.Server;

import com.jacksonpengelly.SharedConfig.ServerInformation;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

import static com.jacksonpengelly.Server.Config.Commands.*;

public class Main {
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
                           // TODO broadcast that server is shutting down
                           System.exit(0);
                           break;
                       case HELP:
                           help();
                           break;
                       default:
                           // TODO broadcast whatever message is sent
                           break;
                   }
               }
            });
            listenerThread.start();
            listenerThread.join();
        } catch (IOException | InterruptedException e) {
            System.err.println("Server Error: " + e.getMessage());
        }
    }

    private static void help() {
        System.out.println("/shutdown | Shutdowns the server for all users.");
        System.out.println("/help | Displays this menu.");
    }

    static void main() {
        start();
    }
}
