/*
 * ServerStatusController.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * ServerStatusController.java
 *
 * @author Nguyen
 */
@RestController
@RequestMapping("/api")
public class ServerStatusController {
    private final WebSocketController webSocketController;

    public ServerStatusController(WebSocketController webSocketController) {
        this.webSocketController = webSocketController;
    }

    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("activeConnections", webSocketController.getActiveSessionCount());
        status.put("status", "running");
        status.put("timestamp", System.currentTimeMillis());
        return status;
    }
}
