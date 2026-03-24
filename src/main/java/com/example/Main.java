package com.example;

import com.example.cli.CommandLineInterface;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: broadcast-server [start|connect]");
            System.out.println("  start  - Start the broadcast server");
            System.out.println("  connect - Connect as a client to the server");
            return;
        }

        String command = args[0];

        if ("start".equals(command)) {
            SpringApplication.run(Main.class, args);
        } else if ("connect".equals(command)) {
            CommandLineInterface.main(args);
        } else {
            System.out.println("Unknown command: " + command);
            System.out.println("Usage: broadcast-server [start|connect]");
        }
    }
}