package com.ufund.api.ufundapi.model;

/**
 * Represents a Basket need entity
 * 
 * @author Ethan Hartman
 */
public class BasketNeed extends Need {
    private int stock;

    /**
     * Create a basket need with the given name, cost, quantity, stock
     * 
     * @param name The name of the need
     * @param cost The unit cost of the need
     * @param quantity How many of the need are in the cart
     * @param stock The stock of the need
     */
    public BasketNeed(String name, double cost, int quantity, int stock) {
        super(name, cost, quantity);
        this.stock = stock;
    } 

    /**
     * Gets the stock of the need
     * 
     * @return stock The stock of the need
     */
    public int getStock() {
        return stock;
    }
}
