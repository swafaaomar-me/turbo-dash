package com.example.turbodash;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Saves and loads player accounts to a binary file ("users.dat") using serialization.
 * Supports any number of accounts; usernames are unique (case-insensitive).
 */
public class UserDataManager {

    public static final String USER_DATA_FILE = "users.dat";
    public static final int MIN_PASSWORD_LENGTH = 6;
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    /** Result of trying to create an account. */
    public enum RegistrationResult {
        SUCCESS, EMPTY_FIELDS, INVALID_EMAIL, PASSWORD_TOO_SHORT, USERNAME_TAKEN, SAVE_FAILED
    }

    private final Path file;

    public UserDataManager() {
        this(AppData.directory().resolve(USER_DATA_FILE));
    }

    public UserDataManager(Path file) {
        this.file = file;
    }

    public RegistrationResult register(String email, String username, String password) {
        email = email == null ? "" : email.trim();
        username = username == null ? "" : username.trim();
        if (email.isEmpty() || username.isEmpty() || password == null || password.isEmpty()) {
            return RegistrationResult.EMPTY_FIELDS;
        }
        if (!EMAIL.matcher(email).matches()) {
            return RegistrationResult.INVALID_EMAIL;
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            return RegistrationResult.PASSWORD_TOO_SHORT;
        }
        List<User> users = loadUsers();
        if (find(users, username).isPresent()) {
            return RegistrationResult.USERNAME_TAKEN;
        }
        users.add(new User(email, username, password));
        try {
            saveUsers(users);
            return RegistrationResult.SUCCESS;
        } catch (IOException e) {
            System.err.println("Error saving user data: " + e.getMessage());
            return RegistrationResult.SAVE_FAILED;
        }
    }

    /** Returns the matching account if the username exists and the password is correct. */
    public Optional<User> authenticate(String username, String password) {
        if (username == null || password == null) {
            return Optional.empty();
        }
        return find(loadUsers(), username.trim()).filter(user -> user.checkPassword(password));
    }

    public List<User> loadUsers() {
        if (!Files.exists(file)) {
            return new ArrayList<>();
        }
        try (InputStream in = Files.newInputStream(file);
             ObjectInputStream objectIn = new ObjectInputStream(in)) {
            @SuppressWarnings("unchecked")
            List<User> users = (List<User>) objectIn.readObject();
            return new ArrayList<>(users);
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            System.err.println("Error loading user data (starting fresh): " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void saveUsers(List<User> users) throws IOException {
        try (OutputStream out = Files.newOutputStream(file);
             ObjectOutputStream objectOut = new ObjectOutputStream(out)) {
            objectOut.writeObject(new ArrayList<>(users));
        }
    }

    private static Optional<User> find(List<User> users, String username) {
        return users.stream().filter(u -> u.getUsername().equalsIgnoreCase(username)).findFirst();
    }
}
