package com.example.turbodash;

import java.io.Serial;
import java.io.Serializable;

/**
 * A player account. Saved to disk with Java serialization by {@link UserDataManager}.
 * Only a salted hash of the password is kept.
 */
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 2L;

    private final String email;
    private final String username;
    private final String passwordSalt;
    private final String passwordHash;

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.passwordSalt = PasswordHasher.newSalt();
        this.passwordHash = PasswordHasher.hash(password, passwordSalt);
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return PasswordHasher.matches(password, passwordSalt, passwordHash);
    }
}
