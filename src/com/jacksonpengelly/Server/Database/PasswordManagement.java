package com.jacksonpengelly.Server.Database;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordManagement {
    public static String hashPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return null;
        }

        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        if (password == null || hashedPassword == null) {
            return false;
        }

        return BCrypt.checkpw(password, hashedPassword);
    }
}