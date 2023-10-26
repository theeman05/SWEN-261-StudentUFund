package com.ufund.api.ufundapi.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Supporter entity
 * 
 * @author Ethan Hartman
 */
public class Supporter extends User {
    static final String STRING_FORMAT = "Supporter [username=%s,isAdmin=%s,fundingBasket=%s]";

    @JsonProperty("funding_basket")
    private String[] fundingBasket;

    /**
     * Create a user with the given username and funding basket
     * Their funding basket will be empty
     * 
     * @param username      The username of the user
     * 
     * @param fundingBasket The list of {@link Need need} keys in this user's
     *                      funding basket
     */
    public Supporter(@JsonProperty("username") String username,
            @JsonProperty("funding_basket") String[] fundingBasket) {
        super(username);
        this.fundingBasket = fundingBasket;
    }

    /**
     * Get the list of {@link Need need} keys in this user's funding basket
     * Note: This should probably only be used on initializding a supporter
     * 
     * @return The list of {@link Need need} keys in this user's funding basket
     */
    public String[] getFundingBasket() {
        return fundingBasket;
    }

    /**
     * Set this supporters funding basket to the given list of {@linkplain Need
     * need} keys.
     * 
     * @param fundingBasket The list of {@link Need need} keys.
     */
    public void setFundingBasket(String[] fundingBasket) {
        this.fundingBasket = fundingBasket;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, username, isAdmin(), Arrays.toString(fundingBasket));
    }
}
