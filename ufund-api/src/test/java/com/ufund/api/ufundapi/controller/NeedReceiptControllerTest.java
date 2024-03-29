package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedReceipt;
import com.ufund.api.ufundapi.persistence.NeedReceiptDAO;

/**
 * Test the Need Receipt Controller class
 * 
 * @author Ethan Hartman
 */
public class NeedReceiptControllerTest {
    private NeedReceiptController needReceiptController;
    private NeedReceiptDAO mockNeedReceiptDao;

    /**
     * Before each test, create a new NeedReceiptController object and inject
     * a mock Need Receipt DAO
     */
    @BeforeEach
    public void setupNeedController() {
        mockNeedReceiptDao = mock(NeedReceiptDAO.class);
        needReceiptController = new NeedReceiptController(mockNeedReceiptDao);
    }

    @Test
    public void testGetNeedReceipt() throws Exception {
        // Setup
        String supporterUsername = "TestUsername";
        String needName = "TestNeed";
        Need testNeed = new Need(needName, 5, 1);
        NeedReceipt testNeedReceipt = new NeedReceipt(supporterUsername, testNeed.getName(), testNeed.getCost(), testNeed.getQuantity());
        when(mockNeedReceiptDao.getReceipt(needName, supporterUsername)).thenReturn(testNeedReceipt);

        // Invoke
        ResponseEntity<NeedReceipt> response = needReceiptController.getNeedReceipt(supporterUsername, needName);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testNeedReceipt, response.getBody());
    }

    @Test
    public void testGetNeedReceipt_Failure() throws IOException {
        // Setup
        String supporterUsername = "TestUsername";
        String needName = "TestNeed";

        // Invoke
        ResponseEntity<NeedReceipt> response = needReceiptController.getNeedReceipt(supporterUsername, needName);
        
        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetNeedReceipts() throws IOException {
        // Setup
        String supporterUsername = "TestUsername";
        String needName = "TestNeed";
        Need testNeed = new Need(needName, 5, 1);
        NeedReceipt testNeedReceipt = new NeedReceipt(supporterUsername, testNeed.getName(), testNeed.getCost(), testNeed.getQuantity());
        when(mockNeedReceiptDao.getReceipts()).thenReturn(new NeedReceipt[] { testNeedReceipt });

        // Invoke
        ResponseEntity<NeedReceipt[]> response = needReceiptController.getNeedReceipts();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testNeedReceipt, response.getBody()[0]);
    }

    @Test
    public void testGetNeedReceipts_Failure() throws IOException {
        // Setup
        doThrow(new IOException()).when(mockNeedReceiptDao).getReceipts();

        // Invoke
        ResponseEntity<NeedReceipt[]> response = needReceiptController.getNeedReceipts();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetNeedReceiptsByUsername() throws IOException {
        // Setup
        String supporterUsername = "TestUsername";
        String needName = "TestNeed";
        Need testNeed = new Need(needName, 5, 1);
        NeedReceipt testNeedReceipt = new NeedReceipt(supporterUsername, testNeed.getName(), testNeed.getCost(), testNeed.getQuantity());
        when(mockNeedReceiptDao.getReceipts(supporterUsername)).thenReturn(new NeedReceipt[] { testNeedReceipt });

        // Invoke
        ResponseEntity<NeedReceipt[]> response = needReceiptController.getNeedReceipts(supporterUsername);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testNeedReceipt, response.getBody()[0]);
    }

    @Test
    public void testGetUserFundingSum() throws IOException {
        // Setup
        String supporterName = "TEst";
        double expected_sum = 123;
        when(mockNeedReceiptDao.getUserFundingSum(supporterName)).thenReturn(expected_sum);

        // Invoke
        ResponseEntity<Double> response = needReceiptController.getUserFundingSum(supporterName);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected_sum, response.getBody());
    }

    @Test
    public void testGetSortedUserFunding() throws IOException {
        // Setup
        String[] expected_user_funding = new String[]{"John"};
        when(mockNeedReceiptDao.getSortedUserFunding()).thenReturn(expected_user_funding);

        // Invoke
        ResponseEntity<String[]> response = needReceiptController.getSortedUserFunding();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected_user_funding, response.getBody());
    }
}
