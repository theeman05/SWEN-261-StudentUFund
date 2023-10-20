package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the User class
 * 
 * @author Ethan Hartman
 */
@Tag("Model-tier")
public class UserTest {
    @Test
    public void testCreateUser() {
        // Setup
        String expected_username = "CoolUser";
        boolean expected_isAdmin = false;

        // Invoke
        User user = new User(expected_username);

        // Analyze
        assertEquals(expected_username, user.getUsername());
        assertEquals(expected_isAdmin, user.isAdmin());
    }

    @Test
    public void testCreateAdmin() {
        // Setup
        String expected_username = "admin";
        boolean expected_isAdmin = true;

        // Invoke
        User user = new User(expected_username);

        // Analyze
        assertEquals(expected_username, user.getUsername());
        assertEquals(expected_isAdmin, user.isAdmin());
    }

    @Test
    public void testToString() {
        // Setup
        String username = "CoolUser";
        boolean isAdmin = false;
        String expected_string = String.format(User.STRING_FORMAT, username, isAdmin);
        User user = new User(username);

        // Invoke
        String actual_string = user.toString();

        // Analyze
        assertEquals(expected_string, actual_string);
    }
}