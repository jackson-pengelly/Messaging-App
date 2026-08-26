package com.jacksonpengelly.Server;

import com.jacksonpengelly.SharedConfig.ServerInformation;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
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

            while (true) {
                Socket client = serverSocket.accept();
                String incomingIP = client.getInetAddress().getHostAddress(); // get incoming ip
                System.out.println("New user connected. IP: " + incomingIP);

                PrintWriter tempOut = new PrintWriter(client.getOutputStream(), true);
                tempOut.println("(Server): Enter your username (this will show for everyone in the room): ");
            }
        } catch (IOException e) {
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
