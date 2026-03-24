/*
 * CommandLineInterface.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.cli;

import com.example.client.BroadcastWebSocketClient;

import java.net.URI;
import java.util.Scanner;

/**
 * CommandLineInterface.java
 *
 * @author Nguyen
 */
public class CommandLineInterface {
    private static final String DEFAULT_SERVER_URL = "ws://localhost:8000/broadcast";
    private static BroadcastWebSocketClient client;

    public static void main(String[] args) {
        String serverUrl = DEFAULT_SERVER_URL;

        // Parse command line arguments
        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("--url") && i + 1 < args.length) {
                serverUrl = args[i + 1];
                break;
            }
        }

        try {
            System.out.println("Broadcast Server Client");
            System.out.println("======================");

            // Get username
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your username (optional, press Enter to skip): ");
            String username = scanner.nextLine();
            if (username.trim().isEmpty()) {
                username = "Anonymous";
            }

            System.out.println("Connecting to " + serverUrl + "...");
            URI serverUri = new URI(serverUrl);
            client = new BroadcastWebSocketClient(serverUri, username);
            client.connect();

            // Wait for connection
            Thread.sleep(1000);

            // Handle user input
            System.out.println("\nEnter your messages (press Enter to send):");
            while (true) {
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                    System.out.println("Disconnecting...");
                    client.close();
                    break;
                }

                if (!input.trim().isEmpty()) {
                    client.sendMessage(input);
                }
            }

            scanner.close();

        } catch (Exception e) {
            System.err.println("Failed to connect: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
