package com.university.management.system;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.nio.file.Files;
import java.util.Properties;
import java.util.List;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProdDBConnectionTest {

    @Test
    public void testConnection() throws Exception {
        File envFile = new File("server/.env");
        if (!envFile.exists()) {
            envFile = new File(".env");
        }

        if (!envFile.exists()) {
            throw new RuntimeException(".env file not found at " + envFile.getAbsolutePath());
        }

        Properties props = new Properties();
        List<String> lines = Files.readAllLines(envFile.toPath());
        for (String line : lines) {
            if (line.trim().isEmpty() || line.startsWith("#"))
                continue;
            String[] parts = line.split("=", 2);
            if (parts.length == 2) {
                props.setProperty(parts[0].trim(), parts[1].trim());
            }
        }

        String url = props.getProperty("DB_URL");
        String user = props.getProperty("DB_USERNAME");
        String pass = props.getProperty("DB_PASSWORD");

        if (url == null || user == null || pass == null) {
            throw new RuntimeException("Missing DB credentials in .env file");
        }

        System.out.println("Attempting connection to: " + url);
        System.out.println("User: " + user);

        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            assertNotNull(connection);
            assertTrue(connection.isValid(5));
            System.out.println("Connection successful!");
        } catch (Exception e) {
            System.err.println("Connection failed: " + e.getMessage());
            throw e;
        }
    }
}
