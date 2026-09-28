package com.example.turbodash;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Keeps each player's high score in a binary file ("scores.dat") using serialization.
 */
public class ScoreManager {

    public static final String SCORE_FILE = "scores.dat";
    private static final String GUEST = "";

    private final Path file;

    public ScoreManager() {
        this(AppData.directory().resolve(SCORE_FILE));
    }

    public ScoreManager(Path file) {
        this.file = file;
    }

    public int getHighScore(String username) {
        return loadScores().getOrDefault(key(username), 0);
    }

    /**
     * Records a finished game. Returns true if it is a new high score for this player.
     */
    public boolean submitScore(String username, int score) {
        Map<String, Integer> scores = loadScores();
        String key = key(username);
        if (score <= scores.getOrDefault(key, 0)) {
            return false;
        }
        scores.put(key, score);
        try (OutputStream out = Files.newOutputStream(file);
             ObjectOutputStream objectOut = new ObjectOutputStream(out)) {
            objectOut.writeObject(scores);
        } catch (IOException e) {
            System.err.println("Error saving score: " + e.getMessage());
        }
        return true;
    }

    private Map<String, Integer> loadScores() {
        if (!Files.exists(file)) {
            return new HashMap<>();
        }
        try (InputStream in = Files.newInputStream(file);
             ObjectInputStream objectIn = new ObjectInputStream(in)) {
            @SuppressWarnings("unchecked")
            Map<String, Integer> scores = (Map<String, Integer>) objectIn.readObject();
            return new HashMap<>(scores);
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            System.err.println("Error loading scores (starting fresh): " + e.getMessage());
            return new HashMap<>();
        }
    }

    private static String key(String username) {
        return username == null ? GUEST : username.toLowerCase();
    }
}
