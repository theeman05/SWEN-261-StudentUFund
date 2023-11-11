package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


/**
 * Testing Need class functions
 * 
 * @author Ben Griffin
 */
@Tag("Model-tier")
public class NeedTest {
    private Need need;

    /**
     * Before each test, create a need with some "default" values
     */
    @BeforeEach
    public void setupNeed() {
        need = new Need("Test 0", 1.00, 0);
    }

    @Test
    public void testSetCost() {
        //setup
        setupNeed();
        this.need.setCost(2.00);
        
        //invoke
        Double expected = 2.00;
        
        //analyze
        assertEquals(expected, need.getCost());
    }

    @Test
    public void setQuantity() {
        //setup
        setupNeed();
        this.need.setQuantity(5);
        
        //invoke
        int expected = 5;
        
        //analyze
        assertEquals(expected ,need.getQuantity());
    }

    @Test
    public void getName() {
        //setup
        setupNeed();

        //invoke
        String expected = "Test 0";
        String actual = need.getName();

        //analyze
        assertEquals(expected, actual);
    }

    @Test
    public void getCost() {
        //setup
        setupNeed();
        
        //invoke
        Double expected = 1.00;
        Double actual = need.getCost();

        //analyze
        assertEquals(expected, actual);
    }

    @Test
    public void getQuantity() {
        //setup
        setupNeed();

        //invoke
        int expected = 0;
        int actual = need.getQuantity();
        
        //analyze
        assertEquals(expected, actual);
    }

    @Test
    public void needToString() {
        //setup
        setupNeed();
        //invoke
        String actual = need.toString();
        String expected = "Need [name=Test 0,cost=$1.0,quantity=0]";
        
        //analyze
        assertEquals(expected, actual);
    }

    @Test
    public void needCompareToGT() {
        //setup
        Need need2 = new Need("Need2", 2.0, 1);
        //invoke
        int result = need.compareTo(need2);
        int expected = -1;
        //analyze
        assertEquals(expected, result);
    }

}
