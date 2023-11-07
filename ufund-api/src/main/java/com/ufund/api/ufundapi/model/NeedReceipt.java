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
     * @param name The name of the funded {@link Need need}
     * 
     * @param totalCost The total cost of the funded {@link Need need}
     * 
     * @param quantity The quantity of the funded {@link Need need}
     * 
     * @param fundedNeed The {@link Need need} which was funded
     */
    public NeedReceipt(@JsonProperty("supporter_username") String supporterUsername, @JsonProperty("name") String name, @JsonProperty("cost") double totalCost, @JsonProperty("quantity") int quantity) {
        super(name, totalCost, quantity);
        this.supporterUsername = supporterUsername;
    }

    /**
     * @return the {@linkplain Supporter supporter}'s username.
     */
    public String getSupporterUsername() {
        return supporterUsername;
    }

    /**
     * Adds the total cost to the current need cost, and the given quantity to the current need quantity.
     * 
     * @param cost The cost to add to the current need cost.
     * 
     * @param quantity The quantity to add to the current need quantity.
     */
    public void fundMore(double cost, int quantity) {
        this.setCost(getCost() + cost * quantity);
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
