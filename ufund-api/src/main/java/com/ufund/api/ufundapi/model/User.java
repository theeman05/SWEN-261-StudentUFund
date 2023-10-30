package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a User entity
 * 
 * @author Ethan Hartman
 */
public class User {
    static final String ADMIN_USERNAME = "admin";
    static final String STRING_FORMAT = "User [name=%s, isAdmin=%s]";
    public static final User ADMIN = new User(ADMIN_USERNAME);

    @JsonProperty("username") protected String username;

    /**
     * Create a user with the given username
     * 
     * @param username The username of the user
     */
    public User(@JsonProperty("username") String username) {
        this.username = username;
    }

    /**
     * Get the username of the user
     * 
     * @return The username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Check if the user is an admin
     * 
     * @return True if the user is an admin, false otherwise
     */
    public boolean isAdmin() {
        return username.equals(ADMIN_USERNAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, username, isAdmin());
    }
}
