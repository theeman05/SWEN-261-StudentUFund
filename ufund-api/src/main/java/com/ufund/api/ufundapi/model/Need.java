package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Need entity
 * 
 * @author Ethan Hartman
 */
public class Need {

    // Package private for tests
    static final String STRING_FORMAT = "Need [name=%s,cost=$%s,quantity=%s]";

    @JsonProperty("name")
    private String name;
    @JsonProperty("cost")
    private double cost;
    @JsonProperty("quantity")
    private int quantity;

    /**
     * Create a need with the given name, cost, quantity
     * 
     * @param name The name of the need
     * @param cost The unit cost of the need
     * @param quantity The quantity of the need
     */
    public Need(@JsonProperty("name") String name, @JsonProperty("cost") double cost,
            @JsonProperty("quantity") int quantity) {
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
    }

    /**
     * Sets the unit cost of the need
     * 
     * @param cost The unit cost of the need
     */

    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * Sets the quantity of the need
     * 
     * @param quantity The quantity of the need
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Retrieves the name of the need
     * 
     * @return The name of the need
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the cost of the need
     * 
     * @return The cost of the need
     */

    public double getCost() {
        return cost;
    }

    /**
     * Retrieves the total cost of the need
     * 
     * @return The total cost of the need
     */
    public double getTotalCost() {
        return cost * quantity;
    }

    /**
     * Retrieves the quantity of the need
     * 
     * @return The quantity of the need
     */

    public int getQuantity() {
        return quantity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, name, cost, quantity);
    }
}
