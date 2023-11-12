package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a NeedMessage entity
 * 
 * @author Ethan Hartman
 */
public class NeedMessage {

    static final String STRING_FORMAT = "NeedMessage [sender_username=%s,message=%s,need_name=%s]";

    @JsonProperty("sender_username") protected String senderUsername;
    @JsonProperty("need_name") protected String needName;
    @JsonProperty("message") protected String message;

    /**
     * Create a Message with the given sender username, the receiver username, and the mssage.
     * 
     * @param senderUsername The sender's username
     * 
     * @param receiverUsername The receiver's username
     * 
     * @param need_name The name of the {@linkplain Need need}.
     * 
     * @param message The message
     */
    public NeedMessage(@JsonProperty("sender_username") String senderUsername, @JsonProperty("need_name") String needName, @JsonProperty("message") String message) {
        this.senderUsername = senderUsername;
        this.message = message;
        this.needName = needName;
    }

    /**
     * @return the name of the {@linkplain Need need}.
     */
    public String getNeedName() {
        return needName;
    }

    

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, senderUsername, message, needName);
    }
}
