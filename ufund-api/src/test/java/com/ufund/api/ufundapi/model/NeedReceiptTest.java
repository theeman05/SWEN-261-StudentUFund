package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;

/**
 * Testing NeedReceiptTest class functions
 * 
 * @author Ethan Hartman
 */
@Tag("Model-tier")
public class NeedReceiptTest {
    @Test
    public void testCreateReceipt() {
        // Setup
        String expectedSupporterName = "CoolUser";
        Need mockNeed = mock(Need.class);

        // Invoke
        NeedReceipt receipt = new NeedReceipt(expectedSupporterName, mockNeed);

        // Analyze
        assertEquals(expectedSupporterName, receipt.getSupporterUsername());
    }

    @Test
    public void testFundMore() {
        // Setup
        double cost_per_item = 10.0;
        int expected_quantity = 3;
        double expected_cost = 30.0;
        NeedReceipt receipt = new NeedReceipt("CoolUser", new Need("Need", cost_per_item, 1));

        // Invoke
        receipt.fundMore(cost_per_item, 2);

        // Analyze
        assertEquals(expected_quantity, receipt.getQuantity());
        assertEquals(expected_cost, receipt.getCost());
    }

    @Test
    public void testToString() {
        // Setup
        String supporterUsername = "CoolUser";
        Need mockNeed = new Need("Need", 1, 1);
        String expected_string = String.format(NeedReceipt.STRING_FORMAT, supporterUsername, mockNeed.toString());
        NeedReceipt receipt = new NeedReceipt(supporterUsername, mockNeed);

        // Invoke
        String actual_string = receipt.toString();

        // Analyze
        assertEquals(expected_string, actual_string);
    }
}
