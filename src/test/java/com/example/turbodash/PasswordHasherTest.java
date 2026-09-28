package com.example.turbodash;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHasherTest {

    @Test
    void matchesOnlyTheRightPassword() {
        String salt = PasswordHasher.newSalt();
        String hash = PasswordHasher.hash("correct horse", salt);
        assertTrue(PasswordHasher.matches("correct horse", salt, hash));
        assertFalse(PasswordHasher.matches("wrong horse", salt, hash));
    }

    @Test
    void samePasswordGetsDifferentHashesWithDifferentSalts() {
        String salt1 = PasswordHasher.newSalt();
        String salt2 = PasswordHasher.newSalt();
        assertNotEquals(salt1, salt2);
        assertNotEquals(PasswordHasher.hash("password", salt1), PasswordHasher.hash("password", salt2));
    }
}
