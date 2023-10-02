package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the type of need
 */
enum NeedType {
    FOOD,
    WATER,
    SHELTER,
    CLOTHING,
    MEDICAL,
    TRANSPORTATION,
    EDUCATION,
    OTHER
}

/**
 * Represents a Need entity
 * 
 * @author Ethan Hartman
 */
public class Need {
    // Package private for tests
    static final String STRING_FORMAT = "Need [name=%s,cost=$%s,quantity=%s,type=%s]";

    @JsonProperty("name") private String name;
    @JsonProperty("cost") private double cost;
    @JsonProperty("quantity") private int quantity;
    @JsonProperty("type") private NeedType type;

    /**
     * Create a need with the given name, cost, quantity, and type
     * @param name The name of the need
     */
    public Need(@JsonProperty("name") String name, @JsonProperty("cost") double cost, @JsonProperty("quantity") int quantity, @JsonProperty("type") NeedType type) {
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.type = type;
    }

    /**
     * Sets the cost of the need
     * @param cost The cost of the need
     */

    public void setCost(double cost) {this.cost = cost;}

    /**
     * Sets the quantity of the need
     * @param quantity The quantity of the need
     */
    public void setQuantity(int quantity) {this.quantity = quantity;}

    /**
     * Sets the type of the need
     * @param type The type of the need
     */

    public void setType(NeedType type) {this.type = type;}

    /**
     * Retrieves the name of the need
     * @return The name of the need
     */
    public String getName() {return name;}

    /**
     * Retrieves the cost of the need
     * @return The cost of the need
     */

    public double getCost() {return cost;}
    /**
     * Retrieves the quantity of the need
     * @return The quantity of the need
     */
    
    public int getQuantity() {return quantity;}

    /**
     * Retrieves the type of the need
     * @return The type of the need
     */
    public NeedType getType() {return type;}

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, name, cost, quantity, type);
    }
}
