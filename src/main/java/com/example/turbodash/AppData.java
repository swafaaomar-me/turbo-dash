package com.example.turbodash;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Where the game keeps its save files.
 *
 * Files are stored in a ".turbodash" folder in the player's home directory, so the game
 * can save data even when it is installed somewhere read-only (like Program Files).
 * Setting the system property "turbodash.dataDir" overrides the location (used by tests).
 */
public final class AppData {

    private AppData() {
    }

    public static Path directory() {
        String override = System.getProperty("turbodash.dataDir");
        Path dir = override != null
                ? Paths.get(override)
                : Paths.get(System.getProperty("user.home"), ".turbodash");
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            System.err.println("Could not create data folder " + dir + ": " + e.getMessage());
        }
        return dir;
    }
}
