package com.ufund.api.ufundapi.exceptions;

/**
 * Defines the exception thrown when a need with the given key is already in the current supporter's basket.
 * 
 * @author Ethan Hartman
 */
public class NeedAlreadyInCartException extends Exception {
    private static final String MESSAGE = "Need with the given key '%s' is already in the basket";
    public NeedAlreadyInCartException(String key) {
        super(String.format(MESSAGE, key));
    }
}
