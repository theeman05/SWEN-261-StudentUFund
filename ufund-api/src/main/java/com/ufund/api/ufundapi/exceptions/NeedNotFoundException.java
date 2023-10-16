package com.ufund.api.ufundapi.exceptions;

/**
 * Defines the exception thrown when a need with the given key is not found
 * 
 * @author Ethan Hartman
 */
public class NeedNotFoundException extends Exception {
    private static final String MESSAGE = "Need with the given key '%s' not found";
    public NeedNotFoundException(String key) {
        super(String.format(MESSAGE, key));
    }
}
