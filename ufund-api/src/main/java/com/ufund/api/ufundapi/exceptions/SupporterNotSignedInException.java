package com.ufund.api.ufundapi.exceptions;

/**
 * Defines the exception thrown when a supporter is not signed in
 * 
 * @author Ethan Hartman
 */
public class SupporterNotSignedInException extends Exception {
    private static final String MESSAGE = "No supporter signed in";
    public SupporterNotSignedInException() {
        super(MESSAGE);
    }
}
