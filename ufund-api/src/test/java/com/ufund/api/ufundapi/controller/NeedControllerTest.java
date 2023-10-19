package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.management.openmbean.KeyAlreadyExistsException;

import com.ufund.api.ufundapi.persistence.NeedDAO;
import com.ufund.api.ufundapi.model.Need;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the Need Controller class
 * 
 * @author Ethan Hartman, Bevan Neiberg
 */
@Tag("Controller-tier")
public class NeedControllerTest {
    private NeedController needController;
    private NeedDAO mockNeedDAO;

    /**
     * Before each test, create a new NeedController object and inject
     * a mock Need DAO
     */
    @BeforeEach
    public void setupNeedController() {
        mockNeedDAO = mock(NeedDAO.class);
        needController = new NeedController(mockNeedDAO);
    }

    @Test
    public void testGetNeed() throws IOException { // getNeed may throw IOException
        // Setup
        Need need = new Need("Test 0", 1.5, 1, Need.NeedType.FOOD);
        // When the same id is passed in, our mock Need DAO will return the Need object
        when(mockNeedDAO.getNeed(need.getName())).thenReturn(need);

        // Invoke
        ResponseEntity<Need> response = needController.getNeed(need.getName());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(need, response.getBody());
    }

    @Test
    public void testGetNeedNotFound() throws Exception { // createNeed may throw IOException
        // Setup
        String needName = "Test 0";
        // When the same id is passed in, our mock Need DAO will return null, simulating
        // no need found
        when(mockNeedDAO.getNeed(needName)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = needController.getNeed(needName);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetNeedHandleException() throws Exception { // createNeed may throw IOException
        // Setup
        String needName = "Test 0";
        // When getNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).getNeed(needName);

        // Invoke
        ResponseEntity<Need> response = needController.getNeed(needName);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /*****************************************************************
     * The following tests will fail until all NeedController methods
     * are implemented.
     ****************************************************************/

    @Test
    public void testCreateNeed() throws IOException { // createNeed may throw IOException
        // Setup
        Need need = new Need("Test 0", 1.5, 1, Need.NeedType.FOOD);
        // when createNeed is called, return true simulating successful
        // creation and save
        when(mockNeedDAO.createNeed(need)).thenReturn(need);

        // Invoke
        ResponseEntity<Need> response = needController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(need, response.getBody());
    }

    @Test
    public void testCreateNeedFailed() throws IOException { // createNeed may throw IOException
        // Setup
        Need need = new Need("Test 0", 3, 15, Need.NeedType.SHELTER);
        // when createNeed is called, return false simulating failed
        // creation and save
        doThrow(new KeyAlreadyExistsException()).when(mockNeedDAO).createNeed(need);

        // Invoke
        ResponseEntity<Need> response = needController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateNeedHandleException() throws IOException { // createNeed may throw IOException
        // Setup
        Need need = new Need("Test 0", 3, 15, Need.NeedType.SHELTER);

        // When createNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).createNeed(need);

        // Invoke
        ResponseEntity<Need> response = needController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateNeed() throws IOException { // updateNeed may throw IOException
        // Setup
        Need need = new Need("Test 0", 312, 19, Need.NeedType.TRANSPORTATION);
        // when updateNeed is called, return true simulating successful
        // update and save
        when(mockNeedDAO.updateNeed(need)).thenReturn(need);
        ResponseEntity<Need> response = needController.updateNeed(need);
        need.setCost(144);

        // Invoke
        response = needController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(need, response.getBody());
    }

    @Test
    public void testUpdateNeedFailed() throws IOException { // updateNeed may throw IOException
        // Setup
        Need need = new Need("Not real need", 1, 1, Need.NeedType.OTHER);
        // when updateNeed is called, return true simulating successful
        // update and save
        when(mockNeedDAO.updateNeed(need)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = needController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateNeedHandleException() throws IOException { // updateNeed may throw IOException
        // Setup
        Need need = new Need("Not real need", 1, 1, Need.NeedType.OTHER);
        // When updateNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).updateNeed(need);

        // Invoke
        ResponseEntity<Need> response = needController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetNeeds() throws IOException { // getNeeds may throw IOException
        // Setup
        Need[] needs = new Need[2];
        needs[0] = new Need("Test 0", 1.5, 1, Need.NeedType.FOOD);
        needs[1] = new Need("Test 1", 3, 15, Need.NeedType.EDUCATION);
        // When getNeeds is called return the needs created above
        when(mockNeedDAO.getNeeds()).thenReturn(needs);

        // Invoke
        ResponseEntity<Need[]> response = needController.getNeeds();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(needs, response.getBody());
    }

    @Test
    public void testGetNeedsHandleException() throws IOException { // getNeeds may throw IOException
        // Setup
        // When getNeeds is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).getNeeds();

        // Invoke
        ResponseEntity<Need[]> response = needController.getNeeds();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSearchNeeds() throws IOException { // findNeeds may throw IOException
        // Setup
        String searchString = "la";
        Need[] needs = new Need[2];
        needs[0] = new Need("Test similar names", 1.5, 1, Need.NeedType.FOOD);
        needs[1] = new Need("Test krampus lair", 3, 15, Need.NeedType.EDUCATION);
        // When findNeeds is called with the search string, return the two
        /// needs above
        when(mockNeedDAO.findNeeds(searchString)).thenReturn(needs);

        // Invoke
        ResponseEntity<Need[]> response = needController.searchNeeds(searchString);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(needs, response.getBody());
    }

    @Test
    public void testSearchNeedsHandleException() throws IOException { // findNeeds may throw IOException
        // Setup
        String searchString = "an";
        // When createNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).findNeeds(searchString);

        // Invoke
        ResponseEntity<Need[]> response = needController.searchNeeds(searchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteNeed() throws IOException { // deleteNeed may throw IOException
        // Setup
        String needName = "Test 0";
        // when deleteNeed is called return true, simulating successful deletion
        when(mockNeedDAO.deleteNeed(needName)).thenReturn(true);

        // Invoke
        ResponseEntity<Need> response = needController.deleteNeed(needName);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteNeedNotFound() throws IOException { // deleteNeed may throw IOException
        // Setup
        String needName = "Test 0";
        // when deleteNeed is called return false, simulating failed deletion
        when(mockNeedDAO.deleteNeed(needName)).thenReturn(false);

        // Invoke
        ResponseEntity<Need> response = needController.deleteNeed(needName);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteNeedHandleException() throws IOException { // deleteNeed may throw IOException
        // Setup
        String needName = "Test 0";
        // When deleteNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).deleteNeed(needName);

        // Invoke
        ResponseEntity<Need> response = needController.deleteNeed(needName);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetNeedByType() throws IOException { // getNeedByType may throw IOException
        // Setup
        Need need = new Need("Test 0", 1.5, 1, Need.NeedType.FOOD);
        // When getNeedByType is called with the type, return the need created above
        when(mockNeedDAO.getNeedByType(need.getType())).thenReturn(need);

        // Invoke
        ResponseEntity<Need> response = needController.getNeedByType(need.getType());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(need, response.getBody());

    }

    @Test
    public void testGetNeedByTypeNotFound() throws IOException { // getNeedByType may throw IOException
        // Setup
        Need.NeedType needType = Need.NeedType.FOOD;
        // When getNeedByType is called with the type, return null, simulating no need
        // found
        when(mockNeedDAO.getNeedByType(needType)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = needController.getNeedByType(needType);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetNeedByTypeHandleException() throws IOException { // getNeedByType may throw IOException
        // Setup
        Need.NeedType needType = Need.NeedType.FOOD;
        // When getNeedByType is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).getNeedByType(needType);

        // Invoke
        ResponseEntity<Need> response = needController.getNeedByType(needType);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetNeedByTypeNotFoundHandleException() throws IOException { // getNeedByType may throw IOException
        // Setup
        Need.NeedType needType = Need.NeedType.FOOD;
        // When getNeedByType is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).getNeedByType(needType);

        // Invoke
        ResponseEntity<Need> response = needController.getNeedByType(needType);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}