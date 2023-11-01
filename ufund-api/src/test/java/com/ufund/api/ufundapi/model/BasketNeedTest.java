package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


/**
 * Testing BasketNeed class functions
 * 
 * @author Ethan Hartman
 */
@Tag("Model-tier")
public class BasketNeedTest {
    private BasketNeed basketNeed;

    /**
     * Before each test, create a basketNeed with some "default" values
     */
    @BeforeEach
    public void setupBasketNeed() {
        basketNeed = new BasketNeed("Test 0", 1, 5, 6);
    }

    @Test
    public void getBasketNeedStock(){
        //setup
        int expected = 6;
        setupBasketNeed();

        //invoke
        int actual = basketNeed.getStock();

        //analyze
        assertEquals(expected, actual);
    }
}
