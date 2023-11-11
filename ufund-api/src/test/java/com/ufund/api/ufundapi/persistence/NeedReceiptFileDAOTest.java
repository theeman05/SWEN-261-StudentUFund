package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedReceipt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the Need Receipt File DAO
 * 
 * @author Ethan Hartman
 */
@Tag("Persistence-tier")
public class NeedReceiptFileDAOTest {
    NeedReceiptFileDAO needReceiptFileDAO;
    NeedReceipt[] testReceipts;
    ObjectMapper mockObjectMapper;
    
    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * 
     * @throws IOException
     */
    @BeforeEach
    public void setUp() throws IOException {
        Need testNeed = new Need("Test0", 5, 1);
        Need testNeed2 = new Need("Test5", 22, 2);
        mockObjectMapper = mock(ObjectMapper.class);
        testReceipts = new NeedReceipt[4];
        testReceipts[0] = new NeedReceipt("testUsername", testNeed.getName(), testNeed.getCost(), testNeed.getQuantity());
        testReceipts[1] = new NeedReceipt("testUsername2", testNeed.getName(), testNeed.getCost(), testNeed.getQuantity());
        testReceipts[2] = new NeedReceipt("testUsername3", testNeed.getName(), testNeed.getCost(), testNeed.getQuantity());
        testReceipts[3] = new NeedReceipt("testUsername3", testNeed2.getName(), testNeed2.getCost() * testNeed2.getQuantity(), testNeed2.getQuantity());

        when(mockObjectMapper
                .readValue(new File("doesnt_matter.txt"), NeedReceipt[].class))
                .thenReturn(testReceipts);
                
        needReceiptFileDAO = new NeedReceiptFileDAO("doesnt_matter.txt", mockObjectMapper);
    }

    @Test
    public void testGetReceipts() throws IOException{
        // Invoke
        NeedReceipt[] actualReceipts = needReceiptFileDAO.getReceipts();

        // Analyze
        assertEquals(testReceipts.length, actualReceipts.length);
        for (int i = 0; i < testReceipts.length; ++i)
            assertEquals(testReceipts[i], actualReceipts[i]);
    }

    @Test
    public void testGetReceiptsForSupporter() throws IOException{
        // Setup
        String supporterUsername = "testUsername";
        int expected_receipts = 1;

        // Invoke
        NeedReceipt[] actualReceipts = needReceiptFileDAO.getReceipts(supporterUsername);

        // Analyze
        for (NeedReceipt receipt : actualReceipts)
            assertEquals(supporterUsername, receipt.getSupporterUsername());
        
        assertEquals(expected_receipts, actualReceipts.length);
    }

    @Test
    public void testGetReceiptsForSupporterWithNoReceipts() throws IOException{
        // Setup
        String supporterUsername = "testUsername4";
        int expected_receipts = 0;

        // Invoke
        NeedReceipt[] actualReceipts = needReceiptFileDAO.getReceipts(supporterUsername);

        // Analyze
        assertEquals(expected_receipts, actualReceipts.length);
    }

    @Test
    public void testGetReceipt() throws IOException{
        // Setup
        String supporterUsername = "testUsername";
        String needName = "Test0";

        // Invoke
        NeedReceipt actualReceipt = needReceiptFileDAO.getReceipt(needName, supporterUsername);

        // Analyze
        assertEquals(supporterUsername, actualReceipt.getSupporterUsername());
        assertEquals(needName, actualReceipt.getName());
    }

    @Test
    public void testGetReceiptWithNoReceipt() throws IOException{
        // Setup
        String supporterUsername = testReceipts[0].getSupporterUsername();
        String needName = "Test1";
        String nonExistingUsername = "testUsername6";

        // Invoke
        NeedReceipt actualReceipt = needReceiptFileDAO.getReceipt(needName, supporterUsername);
        NeedReceipt actualReceipt2 = needReceiptFileDAO.getReceipt(testReceipts[0].getName(), nonExistingUsername);

        // Analyze
        assertNull(actualReceipt);
        assertNull(actualReceipt2);
    }

    @Test
    public void testCreateOrUpdateReceipt() throws IOException{
        // Setup
        String supporterUsername = "testUsername6";
        String needName = "Test1";
        Need need = new Need(needName, 5, 1);

        // Invoke
        NeedReceipt actualReceipt = needReceiptFileDAO.createOrUpdateReceipt(need, supporterUsername);

        // Analyze
        assertEquals(supporterUsername, actualReceipt.getSupporterUsername());
        assertEquals(needName, actualReceipt.getName());
    }

    @Test
    public void testCreateOrUpdateReceiptWithExistingReceipt() throws IOException{
        // Setup
        String supporterUsername = "testUsername";
        String needName = "Test0";
        Need need = new Need(needName, 5, 1);

        // Invoke
        NeedReceipt actualReceipt = needReceiptFileDAO.createOrUpdateReceipt(need, supporterUsername);

        // Analyze
        assertEquals(supporterUsername, actualReceipt.getSupporterUsername());
        assertEquals(needName, actualReceipt.getName());
        assertEquals(10, actualReceipt.getCost());
        assertEquals(2, actualReceipt.getQuantity());
    }

    @Test
    public void testGetSortedReceipts() {
        NeedReceipt[] sortedReceipts =  needReceiptFileDAO.getSortedReceipts();
        Arrays.sort(testReceipts);

        String actual = Arrays.toString(sortedReceipts);
        String expected = Arrays.toString(testReceipts);
        
        assertEquals(expected, actual);
    }
}
