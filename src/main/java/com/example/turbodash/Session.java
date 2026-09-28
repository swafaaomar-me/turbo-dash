package com.example.turbodash;

/** Remembers which player is logged in while the game is open. */
public final class Session {

    private static String currentUsername;

    private Session() {
    }

    public static String currentUsername() {
        return currentUsername;
    }

    public static void logIn(String username) {
        currentUsername = username;
    }

    public static void logOut() {
        currentUsername = null;
    }
}
