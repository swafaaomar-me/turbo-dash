package com.example.turbodash;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ScoreManagerTest {

    @TempDir
    Path tempDir;

    private ScoreManager scores;

    @BeforeEach
    void setUp() {
        scores = new ScoreManager(tempDir.resolve("scores.dat"));
    }

    @Test
    void newPlayerStartsAtZero() {
        assertEquals(0, scores.getHighScore("sam"));
    }

    @Test
    void keepsOnlyTheBestScore() {
        assertTrue(scores.submitScore("sam", 12));
        assertFalse(scores.submitScore("sam", 5));
        assertTrue(scores.submitScore("sam", 20));
        assertEquals(20, scores.getHighScore("sam"));
    }

    @Test
    void scoresAreSeparatePerPlayer() {
        scores.submitScore("alice", 30);
        scores.submitScore("bob", 10);
        assertEquals(30, scores.getHighScore("alice"));
        assertEquals(10, scores.getHighScore("bob"));
    }

    @Test
    void scoresSurviveRestartingTheGame() {
        scores.submitScore("sam", 42);
        ScoreManager reopened = new ScoreManager(tempDir.resolve("scores.dat"));
        assertEquals(42, reopened.getHighScore("sam"));
    }
}
