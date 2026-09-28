package com.example.turbodash;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static com.example.turbodash.UserDataManager.RegistrationResult.*;
import static org.junit.jupiter.api.Assertions.*;

class UserDataManagerTest {

    @TempDir
    Path tempDir;

    private UserDataManager manager;

    @BeforeEach
    void setUp() {
        manager = new UserDataManager(tempDir.resolve("users.dat"));
    }

    @Test
    void registersAndLogsInAUser() {
        assertEquals(SUCCESS, manager.register("sam@example.com", "sam", "secret123"));
        assertTrue(manager.authenticate("sam", "secret123").isPresent());
    }

    @Test
    void usernameIsCaseInsensitiveOnLogin() {
        manager.register("sam@example.com", "Sam", "secret123");
        assertTrue(manager.authenticate("sam", "secret123").isPresent());
    }

    @Test
    void rejectsWrongPasswordAndUnknownUser() {
        manager.register("sam@example.com", "sam", "secret123");
        assertTrue(manager.authenticate("sam", "wrong-password").isEmpty());
        assertTrue(manager.authenticate("nobody", "secret123").isEmpty());
    }

    @Test
    void keepsMultipleAccounts() {
        manager.register("a@example.com", "alice", "password1");
        manager.register("b@example.com", "bob", "password2");
        assertEquals(2, manager.loadUsers().size());
        assertTrue(manager.authenticate("alice", "password1").isPresent());
        assertTrue(manager.authenticate("bob", "password2").isPresent());
    }

    @Test
    void rejectsDuplicateUsername() {
        manager.register("a@example.com", "alice", "password1");
        assertEquals(USERNAME_TAKEN, manager.register("other@example.com", "ALICE", "password2"));
    }

    @Test
    void validatesInput() {
        assertEquals(EMPTY_FIELDS, manager.register("", "sam", "secret123"));
        assertEquals(EMPTY_FIELDS, manager.register("sam@example.com", "  ", "secret123"));
        assertEquals(INVALID_EMAIL, manager.register("not-an-email", "sam", "secret123"));
        assertEquals(PASSWORD_TOO_SHORT, manager.register("sam@example.com", "sam", "abc"));
    }

    @Test
    void neverStoresThePlainPassword() throws Exception {
        manager.register("sam@example.com", "sam", "super-secret-password");
        String fileContents = new String(Files.readAllBytes(tempDir.resolve("users.dat")));
        assertFalse(fileContents.contains("super-secret-password"));
    }

    @Test
    void startsFreshIfTheSaveFileIsCorrupted() throws Exception {
        Files.writeString(tempDir.resolve("users.dat"), "not a valid save file");
        assertTrue(manager.loadUsers().isEmpty());
        assertEquals(SUCCESS, manager.register("sam@example.com", "sam", "secret123"));
    }
}
