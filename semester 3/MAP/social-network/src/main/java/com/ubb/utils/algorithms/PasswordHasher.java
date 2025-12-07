package com.ubb.utils.algorithms;

import com.ubb.config.Config;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;

public class PasswordHasher {

    public static void main(String[] args) throws Exception {
        String url = Config.getProperties().getProperty("DB_URL");
        String username = Config.getProperties().getProperty("DB_USER");
        String password = Config.getProperties().getProperty("DB_PASSWORD");

        Connection conn = DriverManager.getConnection(url, username, password);

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id, password FROM users");

        PreparedStatement updateStmt = conn.prepareStatement(
                "UPDATE users SET password = ? WHERE id = ?"
        );

        while (rs.next()) {
            long id = rs.getLong("id");
            String plainPassword = rs.getString("password");

            // MD5 hash
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(plainPassword.getBytes());
            String hashedPassword = String.format("%032x", new BigInteger(1, hashBytes));

            // Update DB
            updateStmt.setString(1, hashedPassword);
            updateStmt.setLong(2, id);
            updateStmt.executeUpdate();
        }

        updateStmt.close();
        stmt.close();
        conn.close();

        System.out.println("Passwords converted to MD5!");
    }
}

