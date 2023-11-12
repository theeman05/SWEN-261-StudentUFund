package com.ufund.api.ufundapi.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Supporter entity
 * 
 * @author Ethan Hartman
 */
public class Supporter extends User {
    static final String STRING_FORMAT = "Supporter [username=%s,isAdmin=%s,fundingBasket=%s,messages=%s]";

    @JsonProperty("funding_basket")
    private Need[] fundingBasket;

    @JsonProperty("messages")
    private NeedMessage[] messages;

    /**
     * Create a user with the given username and funding basket
     * Their funding basket will be empty
     * 
     * @param username      The username of the user
     * 
     * @param fundingBasket The list of {@link Need needs} in this user's
     *                      funding basket
     */
    public Supporter(@JsonProperty("username") String username,
            @JsonProperty("funding_basket") Need[] fundingBasket, @JsonProperty("messages") NeedMessage[] messages) {
        super(username);
        this.fundingBasket = fundingBasket;
        this.messages = messages;
    }

    /**
     * Get the list of {@link Need needs} in this user's funding basket
     * Note: This should probably only be used on initializding a supporter
     * 
     * @return The list of {@link Need needs} in this user's funding basket
     */
    public Need[] getFundingBasket() {
        return fundingBasket;
    }

    /**
     * Set this supporters funding basket to the given list of {@linkplain Need
     * needs}.
     * 
     * @param fundingBasket The list of {@link Need needs}.
     */
    public void setFundingBasket(Need[] fundingBasket) {
        this.fundingBasket = fundingBasket;
    }

    /**
     * Get the list of {@link NeedMessage messages} in this user's inbox
     * 
     * @return The list of {@link NeedMessage messages} in this user's inbox
     */
    public NeedMessage[] getNeedMessages() {
        return messages;
    }

    /**
     * Set this supporters inbox to the given list of {@linkplain NeedMessage messages}.
     * 
     * @param messages The list of {@link NeedMessage messages}.
     */
    public void setNeedMessages(NeedMessage[] messages) {
        this.messages = messages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, username, isAdmin(), Arrays.toString(fundingBasket), Arrays.toString(messages));
    }
}
