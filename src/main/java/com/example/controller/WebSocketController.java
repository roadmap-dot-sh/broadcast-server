/*
 * WebSocketController.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocketController.java
 *
 * @author Nguyen
 */
@Component
public class WebSocketController extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);
    private static final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String clientId = session.getId();
        sessions.put(clientId, session);
        logger.info("New client connected: {} (Total clients: {})", clientId, sessions.size());

        // Send welcome message
        String welcomeMsg = String.format("[%s] Welcome to the broadcast server! Total clients: %d",
                LocalDateTime.now().format(formatter), sessions.size());
        session.sendMessage(new TextMessage(welcomeMsg));

        // Broadcast join message to all clients
        broadcastMessage(String.format("[%s] Client %s joined the chat",
                LocalDateTime.now().format(formatter), clientId.substring(0, 8)));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String clientId = session.getId();
        String payload = message.getPayload();

        logger.info("Received message from {}: {}", clientId, payload);

        // Format message with timestamp
        String formattedMessage = String.format("[%s] %s: %s",
                LocalDateTime.now().format(formatter),
                clientId.substring(0, 8),
                payload);

        // Broadcast to all connected clients
        broadcastMessage(formattedMessage);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String clientId = session.getId();
        sessions.remove(clientId);
        logger.info("Client disconnected: {} (Total clients: {})", clientId, sessions.size());

        // Broadcast leave message
        broadcastMessage(String.format("[%s] Client %s left the chat (Remaining: %d)",
                LocalDateTime.now().format(formatter),
                clientId.substring(0, 8),
                sessions.size()));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.error("Transport error for session {}: {}", session.getId(), exception.getMessage());
        sessions.remove(session.getId());
        session.close(CloseStatus.SERVER_ERROR);
    }

    private void broadcastMessage(String message) {
        TextMessage textMessage = new TextMessage(message);

        sessions.values().forEach(session -> {
            try {
                if (session.isOpen()) {
                    session.sendMessage(textMessage);
                }
            } catch (IOException e) {
                logger.error("Failed to send message to session {}: {}", session.getId(), e.getMessage());
            }
        });
    }

    public int getActiveSessionCount() {
        return sessions.size();
    }
}
