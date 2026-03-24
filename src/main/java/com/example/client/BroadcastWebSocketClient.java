/*
 * BroadcastWebSocketClient.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.client;

import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.client.WebSocketClient;

import java.net.URI;

/**
 * BroadcastWebSocketClient.java
 *
 * @author Nguyen
 */
public class BroadcastWebSocketClient extends WebSocketClient {

    private String username;

    public BroadcastWebSocketClient(URI serverUri, String username) {
        super(serverUri);
        this.username = username;
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("\n✓ Connected to broadcast server!");
        System.out.println("Type your messages and press Enter to send.");
        System.out.println("Type 'exit' or 'quit' to disconnect.");
        System.out.println("------------------------\n");
    }

    @Override
    public void onMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("\n✗ Disconnected from server: " + reason);
        System.exit(0);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("Error: " + ex.getMessage());
    }

    public void sendMessage(String message) {
        if (message != null && !message.trim().isEmpty()) {
            send(message);
        }
    }
}
