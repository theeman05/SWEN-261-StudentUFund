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
import com.ufund.api.ufundapi.exceptions.NeedAlreadyInCartException;
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
    Supporter[] testSupporter;
    ObjectMapper mockObjectMapper;

    @BeforeEach
    public void setUp() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testSupporter = new Supporter[3];
        String[] basket = new String[0];
        testSupporter[0] = new Supporter("testUsername", basket);
        testSupporter[1] = new Supporter("testUsername2", basket);
        testSupporter[2] = new Supporter("testUsername3", basket);
        when(mockObjectMapper
                .readValue(new File("doesnt_matter.txt"), Supporter[].class))
                .thenReturn(testSupporter);
        NeedDAO needDao = mock(NeedDAO.class);
        when(needDao.getNeed(any())).thenReturn(new Need("testNeed", 1.5, 1, Need.NeedType.FOOD));
        userFileDAO = new UserFileDAO("doesnt_matter.txt", mockObjectMapper, needDao);
    }

    @Test
    public void testCreateSupporter_alreadyExists() throws KeyAlreadyExistsException, IOException {
        Supporter mockSupporter = mock(Supporter.class);
        when(mockSupporter.getUsername()).thenReturn("testUsername");

        // Simulating a scenario where a supporter with the given username already
        // exists

        // Invoke
        assertThrows(KeyAlreadyExistsException.class, () -> {
            userFileDAO.createSupporter(mockSupporter);
        });

        assertThrows(KeyAlreadyExistsException.class, () -> {
            userFileDAO.createSupporter(mockSupporter);
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
    public void testLoginUser_failure() throws IOException {
        // Setup
        Supporter supporter = new Supporter("testUsername4", new String[0]);

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
            throws IOException, NeedNotFoundException, NeedAlreadyInCartException, SupporterNotSignedInException {
        // Setup
        Supporter supporter = testSupporter[0];
        userFileDAO.loginUser(supporter);

        // Invoke
        Need need = new Need("testNeed1", 1.5, 1, Need.NeedType.FOOD);
        userFileDAO.addNeedToCurBasket(need.getName());
        // Analyze
        assertEquals(1, userFileDAO.getCurBasket().length);

    }

    @Test
    public void testAddNeedToCurBasket_failure()
            throws IOException, NeedNotFoundException, NeedAlreadyInCartException, SupporterNotSignedInException {
        // Setup
        Supporter supporter = testSupporter[0];
        userFileDAO.loginUser(supporter);

        // Invoke
        Need need = new Need("testNeed1", 1.5, 1, Need.NeedType.FOOD);
        Need need2 = new Need("testNeed1", 1.5, 1, Need.NeedType.FOOD);
        userFileDAO.addNeedToCurBasket(need.getName());
        // Analyze
        assertThrows(NeedAlreadyInCartException.class, () -> {
            userFileDAO.addNeedToCurBasket(need2.getName());
        });
    }

    @Test
    public void testRemoveNeedFromCurBasket_success()
            throws IOException, NeedNotFoundException, NeedAlreadyInCartException, SupporterNotSignedInException {
        // Setup
        Supporter supporter = testSupporter[0];
        userFileDAO.loginUser(supporter);

        // Invoke
        Need need = new Need("testNeed1", 1.5, 1, Need.NeedType.FOOD);
        userFileDAO.addNeedToCurBasket(need.getName());
        userFileDAO.removeNeedFromCurBasket(need.getName());
        // Analyze
        assertEquals(0, userFileDAO.getCurBasket().length);

    }

    @Test
    public void testAddNeedToCurBasket_NeedNotFound_Failure()
            throws IOException, SupporterNotSignedInException, NeedAlreadyInCartException, NeedNotFoundException {
        // Setup
        Supporter supporter = testSupporter[0];
        userFileDAO.loginUser(supporter);

        // Analyze
        assertThrows(NeedNotFoundException.class, () -> {
            userFileDAO.addNeedToCurBasket("testNeed2342");
            userFileDAO.removeNeedFromCurBasket("testNeed23412");
        });
    }
}