package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import javax.management.openmbean.KeyAlreadyExistsException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.exceptions.NeedNotFoundException;
import com.ufund.api.ufundapi.exceptions.SupporterNotSignedInException;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Supporter;
import com.ufund.api.ufundapi.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the User File DAO class
 * 
 * @author Bevan Neiberg
 */
@Tag("Persistence-tier")
public class UserFileDAOTests {

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * 
     * @throws IOException
     */

    UserFileDAO userFileDAO;
    NeedDAO mockNeedDao;
    Supporter[] testSupporter;
    ObjectMapper mockObjectMapper;

    @BeforeEach
    public void setUp() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testSupporter = new Supporter[3];
        Need[] basket = new Need[0];
        testSupporter[0] = new Supporter("testUsername", basket);
        testSupporter[1] = new Supporter("testUsername2", basket);
        testSupporter[2] = new Supporter("testUsername3", basket);
        when(mockObjectMapper
                .readValue(new File("doesnt_matter.txt"), Supporter[].class))
                .thenReturn(testSupporter);
        mockNeedDao = mock(NeedDAO.class);
        userFileDAO = new UserFileDAO("doesnt_matter.txt", mockObjectMapper, mockNeedDao);
    }

    @Test
    public void testCreateSupporter_alreadyExists() throws KeyAlreadyExistsException, IOException {
        // Setup
        Supporter mock_admin = mock(Supporter.class);
        when(mock_admin.isAdmin()).thenReturn(true);
         
        // Invoke
        assertThrows(KeyAlreadyExistsException.class, () -> {
            userFileDAO.createSupporter(testSupporter[0]);
        });

        assertThrows(KeyAlreadyExistsException.class, () -> {
            userFileDAO.createSupporter(mock_admin);
        });
    }

    @Test
    public void testCreateSupporter_success() throws KeyAlreadyExistsException, IOException {
        Supporter mockSupporter = mock(Supporter.class);
        when(mockSupporter.getUsername()).thenReturn("testUsername4");

        // Simulating a scenario where a supporter with the given username already
        // exists

        // Invoke
        assertDoesNotThrow(() -> {
            userFileDAO.createSupporter(mockSupporter);
        });
    }

    @Test
    public void testGetSupporter_success() throws IOException {
        // Invoke
        User supporter = userFileDAO.getUser(testSupporter[0].getUsername());

        // Analyze
        assertEquals(supporter, testSupporter[0]);
    }

    @Test
    public void testGetSupporter_admin() throws IOException {
        // Invoke
        User user = userFileDAO.getUser(User.ADMIN.getUsername());

        // Analyze
        assertEquals(User.ADMIN, user);
    }

    @Test
    public void testGetSupporter_failure() throws IOException {
        // Invoke
        User supporter = userFileDAO.getUser("testUsername4");

        // Analyze
        assertNull(supporter);
    }

    @Test
    public void testLoginUser_success() throws IOException {
        // Setup
        Supporter supporter = testSupporter[0];

        // Invoke
        boolean result1 = userFileDAO.loginUser(supporter);

        // Analyze
        assertTrue(result1);
        assertEquals(supporter, userFileDAO.getCurUser());
    }

    @Test
    public void testLoginUser_sameUser() throws IOException {
        // Setup
        Supporter supporter = testSupporter[0];
        userFileDAO.loginUser(supporter);

        // Invoke
        boolean result1 = userFileDAO.loginUser(supporter);

        // Analyze
        assertTrue(result1);
    }

    @Test
    public void testLoginUser_admin() throws IOException {
        // Invoke
        boolean result = userFileDAO.loginUser(User.ADMIN);

        // Analyze
        assertTrue(result);
        assertEquals(User.ADMIN, userFileDAO.getCurUser());
    }

    @Test
    public void testLoginUser_failure() throws IOException {
        // Setup
        Supporter supporter = new Supporter("testUsername4", new Need[0]);

        // Invoke
        boolean result1 = userFileDAO.loginUser(supporter);

        // Analyze
        assertFalse(result1);
        assertNull(userFileDAO.getCurUser());
    }

    @Test
    public void testLogoutCurUser() throws IOException {
        // Setup
        Supporter supporter = testSupporter[0];
        userFileDAO.loginUser(supporter);

        // Invoke
        userFileDAO.logoutCurUser();

        // Analyze
        assertNull(userFileDAO.getCurUser());
    }

    @Test
    public void testAddNeedToCurBasket_success()
            throws IOException, NeedNotFoundException, SupporterNotSignedInException {
        // Setup
        Need expected_need = new Need("testNeed1", 1.5, 12);
        Supporter supporter = testSupporter[0];
        userFileDAO.loginUser(supporter);
        when(mockNeedDao.getNeed(any())).thenReturn(expected_need);

        // Invoke
        userFileDAO.addNeedToCurBasket(expected_need.getName(), 1);

        // Analyze
        assertEquals(1, userFileDAO.getCurBasket().length);
    }

    @Test
    public void testAddNeedToCurBasket_SupporterNotSignedIn()
            throws IOException, NeedNotFoundException, SupporterNotSignedInException {
        // Analyze
        assertThrows(SupporterNotSignedInException.class, () -> {
            userFileDAO.addNeedToCurBasket("", 12);
        });
    }

    @Test
    public void testAddNeedToCurBasket_NeedNotFound()
            throws IOException, NeedNotFoundException, SupporterNotSignedInException {
        // Setup
        Supporter supporter = testSupporter[0];
        userFileDAO.loginUser(supporter);
        
        // Analyze
        assertThrows(NeedNotFoundException.class, () -> {
            userFileDAO.addNeedToCurBasket("Not in basket obviously", 1);
        });
    }    

    @Test
    public void testUpdateNeedInCurBasket_success()
            throws IOException, NeedNotFoundException, SupporterNotSignedInException {
        // Setup
        Need need = new Need("testNeed1", 1.5, 6);
        Supporter supporter = testSupporter[0];
        userFileDAO.loginUser(supporter);

        when(mockNeedDao.getNeed(need.getName())).thenReturn(need);
        
        // Invoke
        userFileDAO.addNeedToCurBasket(need.getName(), 1);
        userFileDAO.updateNeedInCurBasket(need.getName(), 0);

        // Analyze
        assertEquals(0, userFileDAO.getCurBasket().length);
    }

    @Test
    public void testUpdateNeedInCurBasket_NeedNotFound()
            throws IOException, SupporterNotSignedInException, NeedNotFoundException {
        // Setup
        Supporter supporter = testSupporter[0];
        userFileDAO.loginUser(supporter);

        // Analyze
        assertThrows(NeedNotFoundException.class, () -> {
            userFileDAO.updateNeedInCurBasket("testNeed23412", 1);
        });
    }

    @Test
    public void testUpdateNeedInCurBasket_SupporterNotSignedIn()
            throws IOException, SupporterNotSignedInException, NeedNotFoundException {
        // Analyze
        assertThrows(SupporterNotSignedInException.class, () -> {
            userFileDAO.updateNeedInCurBasket("testNeed23412", 1);
        });
    }

    @Test
    public void testGetCurBasket_SupporterNotSignedIn() throws SupporterNotSignedInException {
        // Analyze
        assertThrows(SupporterNotSignedInException.class, () -> {
            userFileDAO.getCurBasket();
        });
    }

    @Test
    public void testCheckoutBasket() throws IOException, SupporterNotSignedInException, NeedNotFoundException {
        // Setup
        Need expected_need = new Need("testNeed1", 1.5, 5);
        when(mockNeedDao.getNeed(any())).thenReturn(expected_need);

        userFileDAO.loginUser(testSupporter[0]);
        userFileDAO.addNeedToCurBasket(expected_need.getName(), 5);

        // Invoke
        userFileDAO.checkoutCurBasket();

        // Analyze
        assertEquals(userFileDAO.getCurBasket().length, 0);
    }

    @Test
    public void testCheckoutBasket_SupporterNotSignedIn() throws SupporterNotSignedInException {
        // Analyze
        assertThrows(SupporterNotSignedInException.class, () -> {
            userFileDAO.checkoutCurBasket();
        });
    }

    @Test
    public void testGetBasketable() throws SupporterNotSignedInException, IOException, NeedNotFoundException {
        // Setup
        Need[] available_needs = new Need[3];
        available_needs[0] = new Need("testNeed1", 1.5, 2);
        available_needs[1] = new Need("testNeed2", 2, 21);
        available_needs[2] = new Need("testNeed3", 3, 3);
        when(mockNeedDao.getNeeds()).thenReturn(available_needs);
        when(mockNeedDao.getNeed(available_needs[0].getName())).thenReturn(available_needs[2]);

        userFileDAO.loginUser(testSupporter[0]);
        userFileDAO.addNeedToCurBasket(available_needs[0].getName(), 2);

        int expected_available_size = 2;

        // Invoke
        Need[] basketable_needs = userFileDAO.getBasketableNeeds();

        // Analyze
        assertEquals(expected_available_size, basketable_needs.length);
    }

    @Test
    public void testGetBasketable_SupporterNotSignedIn() throws SupporterNotSignedInException {
        // Analyze
        assertThrows(SupporterNotSignedInException.class, () -> {
            userFileDAO.getBasketableNeeds();
        });
    }
}