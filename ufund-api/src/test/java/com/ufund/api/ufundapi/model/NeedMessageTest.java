package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Testing Need Message class functions
 * 
 * @author Ethan Hartman
 */
@Tag("Model-tier")
public class NeedMessageTest {
    private NeedMessage needMessage;

    /**
     * Before each test, create a basketNeed with some "default" values
     */
    @BeforeEach
    public void setupBasketNeed() {
        needMessage = new NeedMessage("Sender", "Dog feet", "Thakns for donating the dog feeet!");
    }

    @Test
    public void testGetNeedName() {
        // Setup
        String expected = "Dog feet";
        setupBasketNeed();

        // Invoke
        String actual = needMessage.getNeedName();

        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        // Setup
        String expected = "NeedMessage [sender_username=Sender,message=Thakns for donating the dog feeet!,need_name=Dog feet]";
        setupBasketNeed();

        // Invoke
        String actual = needMessage.toString();

        // Analyze
        assertEquals(expected, actual);
    }
}
