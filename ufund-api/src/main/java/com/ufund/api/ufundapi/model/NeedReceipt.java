package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Need Receipt entity
 * 
 * @author Ethan Hartman
 */
public class NeedReceipt extends Need {
    static final String STRING_FORMAT = "Need Receipt [supporter username=%s, %s]";

    @JsonProperty("supporter_username") protected String supporterUsername;

    /**
     * Create a Need Receipt with the given {@linkplain Supporter supporter}'s username and the {@linkplain Need need} they funded
     * 
     * Note: The need's cost is now the total cost funded, no longer calculated by quantity * cost.
     * 
     * @param supporterUsername The {@link Supporter supporter}'s username
     * 
     * @param fundedNeed The {@link Need need} which was funded
     */
    public NeedReceipt(@JsonProperty("supporter_username") String supporterUsername, @JsonProperty("funded_need") Need fundedNeed) {
        super(fundedNeed.getName(), fundedNeed.getCost() * fundedNeed.getQuantity(), fundedNeed.getQuantity());
        this.supporterUsername = supporterUsername;
    }

    /**
     * @return the {@linkplain Supporter supporter}'s username.
     */
    public String getSupporterUsername() {
        return supporterUsername;
    }

    /**
     * Adds the given total cost to the current need cost, and the given quantity to the current need quantity.
     * 
     * @param totalCost The total cost to add to the current need cost.
     * 
     * @param quantity The quantity to add to the current need quantity.
     */
    public void fundMore(double totalCost, int quantity) {
        this.setCost(getCost() + totalCost);
        this.setQuantity(getQuantity() + quantity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, supporterUsername, super.toString());
    }
}
