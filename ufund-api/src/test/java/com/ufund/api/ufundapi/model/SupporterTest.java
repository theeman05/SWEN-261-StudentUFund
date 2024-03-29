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
        NeedMessage[] expected_messages = new NeedMessage[]{new NeedMessage("CoolSupporter", "Cheese", "I like cheese")};
        boolean expected_isAdmin = false;

        // Invoke
        Supporter supporter = new Supporter(expected_username, expected_funding_basket, expected_messages);

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
        NeedMessage[] expected_messages = new NeedMessage[]{new NeedMessage("CoolSupporter", "Cheese", "I like cheese")};
        Supporter supporter = new Supporter(username, original_basket, expected_messages);

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
        NeedMessage[] expected_messages = new NeedMessage[]{new NeedMessage("CoolSupporter", "Cheese", "I like cheese")};
        String expected_string = String.format(Supporter.STRING_FORMAT, username, isAdmin, Arrays.toString(funding_basket), Arrays.toString(expected_messages));
        Supporter supporter = new Supporter(username, funding_basket, expected_messages);

        // Invoke
        String actual_string = supporter.toString();

        // Analyze
        assertEquals(expected_string, actual_string);
    }

    @Test
    public void testGetNeedMessages() {
        // Setup
        String username = "CoolSupporter";
        Need[] funding_basket = new Need[]{new Need("Cheese", 1, 1), new Need("Bread", 1, 1)};
        NeedMessage[] expected_messages = new NeedMessage[]{new NeedMessage("CoolSupporter", "Cheese", "I like cheese")};
        Supporter supporter = new Supporter(username, funding_basket, expected_messages);

        // Invoke
        NeedMessage[] actual_messages = supporter.getNeedMessages();

        // Analyze
        assertEquals(expected_messages, actual_messages);
    }
}