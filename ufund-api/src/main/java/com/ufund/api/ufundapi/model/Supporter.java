package com.ufund.api.ufundapi.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Supporter entity
 * 
 * @author Ethan Hartman
 */
public class Supporter extends User{
    static final String STRING_FORMAT = "Supporter [name=%s,type=%s,fundingBasket=%s]";

    @JsonProperty("funding_basket") private Need[] fundingBasket;

    /**
     * Create a user with the given username and type
     * Their funding basket will be empty
     * @param username The username of the user
     * @param fundingBasket The list of {@linkplain Need needs} in this user's funding basket
     */
    public Supporter(@JsonProperty("username") String username, @JsonProperty("funding_basket") Need[] fundingBasket) {
        super(username);
        this.fundingBasket = fundingBasket;
    }

    /**
     * Get the list of {@linkplain Need needs} in this user's funding basket
     * Note: This should probably only be used on initializding a supporter
     * @return The list of {@linkplain Need needs} in this user's funding basket
     */
    public Need[] getFundingBasket() {
        return fundingBasket;
    }

    /**
     * Set the list of {@linkplain Need needs} in this user's funding basket
     * @param fundingBasket The list of {@linkplain Need needs} in this user's funding basket
     */
    public void setFundingBasket(Need[] fundingBasket) {
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
