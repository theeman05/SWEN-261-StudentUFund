package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the Supporter class
 * 
 * @author Ethan Hartman
 */
@Tag("Model-tier")
public class SupporterTest {
    @Test
    public void testCreateSupporter() {
        // Setup
        String expected_username = "CoolSupporter";
        Need[] expected_funding_basket = new Need[]{new Need("Cheese", 1, 1), new Need("Bread", 1, 1)};
        boolean expected_isAdmin = false;

        // Invoke
        Supporter supporter = new Supporter(expected_username, expected_funding_basket);

        // Analyze
        assertEquals(expected_username, supporter.getUsername());
        assertEquals(expected_isAdmin, supporter.isAdmin());
        assertEquals(expected_funding_basket, supporter.getFundingBasket());
    }

    @Test
    public void testSetFundingBasket() {
        // Setup
        String username = "CoolSupporter";
        Need[] original_basket = new Need[1];
        Need[] expected_funding_basket = new Need[]{new Need("Cheese", 1, 1), new Need("Bread", 1, 1)};
        Supporter supporter = new Supporter(username, original_basket);

        // Invoke
        supporter.setFundingBasket(expected_funding_basket);

        // Analyze
        assertEquals(expected_funding_basket, supporter.getFundingBasket());
    }

    @Test
    public void testToString() {
        // Setup
        String username = "CoolSupporter";
        boolean isAdmin = false;
        Need[] funding_basket = new Need[]{new Need("Cheese", 1, 1), new Need("Bread", 1, 1)};
        String expected_string = String.format(Supporter.STRING_FORMAT, username, isAdmin, Arrays.toString(funding_basket));
        Supporter supporter = new Supporter(username, funding_basket);

        // Invoke
        String actual_string = supporter.toString();

        // Analyze
        assertEquals(expected_string, actual_string);
    }
}