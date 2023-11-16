package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import javax.management.openmbean.KeyAlreadyExistsException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.exceptions.NeedNotFoundException;
import com.ufund.api.ufundapi.exceptions.SupporterNotSignedInException;
import com.ufund.api.ufundapi.model.BasketNeed;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedMessage;
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
    NeedReceiptDAO mockNeedReceiptDao;
    NeedDAO mockNeedDao;
    Supporter[] testSupporter;
    ObjectMapper mockObjectMapper;

    @BeforeEach
    public void setUp() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testSupporter = new Supporter[3];
        NeedMessage[] expected_messages = new NeedMessage[]{new NeedMessage("CoolSupporter", "Cheese", "I like cheese")};
        Need[] basket = new Need[0];
        testSupporter[0] = new Supporter("testUsername", basket, expected_messages);
        testSupporter[1] = new Supporter("testUsername2", basket, expected_messages);
        testSupporter[2] = new Supporter("testUsername3", basket, expected_messages);
        when(mockObjectMapper
                .readValue(new File("doesnt_matter.txt"), Supporter[].class))
                .thenReturn(testSupporter);
                
        mockNeedDao = mock(NeedDAO.class);
        mockNeedReceiptDao = mock(NeedReceiptDAO.class);
        userFileDAO = new UserFileDAO("doesnt_matter.txt", mockObjectMapper, mockNeedDao, mockNeedReceiptDao);
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
        Need[] basket = new Need[]{new Need("Test1", 1, 5), new Need("Test2", 2, 3)};
        Supporter supporter = testSupporter[0];
        supporter.setFundingBasket(basket);

        when(mockNeedDao.getNeed(basket[1].getName())).thenReturn(null);

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
        Supporter supporter = new Supporter("testUsername4", new Need[0], new NeedMessage[0]);

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
        userFileDAO.updateNeedInCurBasket(expected_need.getName(), 1);

        // Analyze
        assertEquals(1, userFileDAO.getCurBasket().length);
    }

    @Test
    public void testAddExistingNeedToCurBasket_success()
            throws IOException, NeedNotFoundException, SupporterNotSignedInException {
        // Setup
        Need expected_need = new Need("testNeed1", 1.5, 12);
        Supporter supporter = testSupporter[0];
        userFileDAO.loginUser(supporter);
        when(mockNeedDao.getNeed(any())).thenReturn(expected_need);
        userFileDAO.updateNeedInCurBasket(expected_need.getName(), 1);

        // Invoke
        userFileDAO.updateNeedInCurBasket(expected_need.getName(), 1);

        // Analyze
        assertEquals(1, userFileDAO.getCurBasket().length);
    }


    @Test
    public void testAddNeedToCurBasket_SupporterNotSignedIn()
            throws IOException, NeedNotFoundException, SupporterNotSignedInException {
        // Analyze
        assertThrows(SupporterNotSignedInException.class, () -> {
            userFileDAO.updateNeedInCurBasket("", 12);
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
            userFileDAO.updateNeedInCurBasket("Not in basket obviously", 1);
        });
    }    

    @Test
    public void testUpdateNeedInCurBasketRemoveQuant_success()
            throws IOException, NeedNotFoundException, SupporterNotSignedInException {
        // Setup
        Need need = new Need("testNeed1", 1.5, 6);
        Supporter supporter = testSupporter[0];
        userFileDAO.loginUser(supporter);

        when(mockNeedDao.getNeed(need.getName())).thenReturn(need);
        
        // Invoke
        userFileDAO.updateNeedInCurBasket(need.getName(), 1);
        userFileDAO.updateNeedInCurBasket(need.getName(), 0);

        // Analyze
        assertEquals(0, userFileDAO.getCurBasket().length);
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
        userFileDAO.updateNeedInCurBasket(need.getName(), 1);
        userFileDAO.updateNeedInCurBasket(need.getName(), 3);

        // Analyze
        assertEquals(1, userFileDAO.getCurBasket().length);
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
    public void testCheckoutBasketBuyAll() throws IOException, SupporterNotSignedInException, NeedNotFoundException {
        // Setup
        Need expected_need = new Need("testNeed1", 1.5, 5);
        when(mockNeedDao.getNeed(any())).thenReturn(expected_need);

        userFileDAO.loginUser(testSupporter[0]);
        userFileDAO.updateNeedInCurBasket(expected_need.getName(), 5);

        // Invoke
        userFileDAO.checkoutCurBasket();

        // Analyze
        assertEquals(userFileDAO.getCurBasket().length, 0);
    }

    @Test
    public void testCheckoutBasketBuyFew() throws IOException, SupporterNotSignedInException, NeedNotFoundException {
        // Setup
        Need expected_need = new Need("testNeed1", 1.5, 5);
        when(mockNeedDao.getNeed(any())).thenReturn(expected_need);

        userFileDAO.loginUser(testSupporter[0]);
        userFileDAO.updateNeedInCurBasket(expected_need.getName(), 3);

        // Invoke
        userFileDAO.checkoutCurBasket();

        // Analyze
        assertEquals(2, expected_need.getQuantity());
    }

    @Test
    public void testCheckoutBasketNeedQuantDecrease() throws IOException, SupporterNotSignedInException, NeedNotFoundException {
        // Setup
        Need updated_need = new Need("testNeed1", 1.5, 5);
        when(mockNeedDao.getNeed(any())).thenReturn(updated_need);

        userFileDAO.loginUser(testSupporter[0]);
        userFileDAO.updateNeedInCurBasket(updated_need.getName(), 5);

        updated_need.setQuantity(3);
        when(mockNeedDao.getNeed(any())).thenReturn(updated_need);

        // Invoke
        boolean response = userFileDAO.checkoutCurBasket();

        // Analyze
        assertFalse(response);
    }

    @Test
    public void testCheckoutBasketNeedRemoved() throws IOException, SupporterNotSignedInException, NeedNotFoundException {
        // Setup
        Need updated_need = new Need("testNeed1", 1.5, 5);
        when(mockNeedDao.getNeed(any())).thenReturn(updated_need);

        userFileDAO.loginUser(testSupporter[0]);
        userFileDAO.updateNeedInCurBasket(updated_need.getName(), 5);

        when(mockNeedDao.getNeed(any())).thenReturn(null);

        // Invoke
        boolean response = userFileDAO.checkoutCurBasket();

        // Analyze
        assertFalse(response);
    }

    @Test
    public void testCheckoutBasketNeedCostIncrease() throws IOException, SupporterNotSignedInException, NeedNotFoundException {
        // Setup
        Need updated_need = new Need("testNeed1", 1.5, 5);
        when(mockNeedDao.getNeed(any())).thenReturn(updated_need);

        userFileDAO.loginUser(testSupporter[0]);
        userFileDAO.updateNeedInCurBasket(updated_need.getName(), 5);

        updated_need.setCost(3);
        when(mockNeedDao.getNeed(any())).thenReturn(updated_need);

        // Invoke
        boolean response = userFileDAO.checkoutCurBasket();

        // Analyze
        assertFalse(response);
    }

    @Test
    public void testCheckoutBasketWithNonMatchingNeed() throws IOException, SupporterNotSignedInException, NeedNotFoundException {
        // Setup
        Need expected_need = new Need("testNeed1", 1.5, 5);
        Need non_expected_need = new Need("testNeed2", 3, 8);
        when(mockNeedDao.getNeed(expected_need.getName())).thenReturn(expected_need);
        when(mockNeedDao.getNeed(non_expected_need.getName())).thenReturn(non_expected_need);
        int expected_new_quantity = 1;

        userFileDAO.loginUser(testSupporter[0]);
        userFileDAO.updateNeedInCurBasket(expected_need.getName(), 4);
        userFileDAO.updateNeedInCurBasket(non_expected_need.getName(), 5);
        when(mockNeedDao.getNeed(non_expected_need.getName())).thenReturn(null);

        // Invoke
        boolean response = userFileDAO.checkoutCurBasket();

        // Analyze
        assertFalse(response);
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
        available_needs[2] = new Need("testNeed3", 5, 12);
        when(mockNeedDao.getNeeds()).thenReturn(available_needs);
        when(mockNeedDao.getNeed(available_needs[0].getName())).thenReturn(available_needs[0]);
        when(mockNeedDao.getNeed(available_needs[1].getName())).thenReturn(available_needs[1]);
        when(mockNeedDao.getNeed(available_needs[2].getName())).thenReturn(available_needs[2]);

        userFileDAO.loginUser(testSupporter[0]);
        userFileDAO.updateNeedInCurBasket(available_needs[1].getName(), 3);
        userFileDAO.updateNeedInCurBasket(available_needs[2].getName(), 26);

        int expected_available_size = 2;
        int expected_need_2_quantity = 21 - 3;

        // Invoke
        Need[] basketable_needs = userFileDAO.getBasketableNeeds();

        // Analyze
        assertEquals(expected_available_size, basketable_needs.length);
        assertEquals(basketable_needs[1].getQuantity(), expected_need_2_quantity);
    }

    @Test
    public void testGetBasketable_SupporterNotSignedIn() throws SupporterNotSignedInException {
        // Analyze
        assertThrows(SupporterNotSignedInException.class, () -> {
            userFileDAO.getBasketableNeeds();
        });
    }

    @Test
    public void testGetBasketNeed() throws SupporterNotSignedInException, IOException, NeedNotFoundException {
        // Setup
        Need needInBasket = new Need("in", 1, 15);
        when(mockNeedDao.getNeed(needInBasket.getName())).thenReturn(needInBasket);

        int expected_quantity = 5;

        userFileDAO.loginUser(testSupporter[0]);
        userFileDAO.updateNeedInCurBasket(needInBasket.getName(), expected_quantity);

        // Invoke
        BasketNeed basketNeed = userFileDAO.getBasketOrNormalNeed(needInBasket.getName());

        // Analyze
        assertEquals(needInBasket.getName(), basketNeed.getName());
        assertEquals(expected_quantity, basketNeed.getQuantity());
        assertEquals(basketNeed.getStock(), needInBasket.getQuantity());
    }

    @Test
    public void testGetBasketNeed2() throws SupporterNotSignedInException, IOException, NeedNotFoundException {
        // Setup
        Need needInBasket = new Need("in", 1, 15);
        when(mockNeedDao.getNeed(needInBasket.getName())).thenReturn(needInBasket);

        int expected_quantity = 0;

        userFileDAO.loginUser(testSupporter[0]);

        // Invoke
        BasketNeed basketNeed = userFileDAO.getBasketOrNormalNeed(needInBasket.getName());

        // Analyze
        assertEquals(needInBasket.getName(), basketNeed.getName());
        assertEquals(expected_quantity, basketNeed.getQuantity());
        assertEquals(basketNeed.getStock(), needInBasket.getQuantity());
    }

    @Test
    public void testGetBasketNeed_SupporterNotSignedInException() throws SupporterNotSignedInException, IOException, NeedNotFoundException {
        // Analyze
        assertThrows(SupporterNotSignedInException.class, () -> {
            userFileDAO.getBasketOrNormalNeed("");
        });
    }

    @Test
    public void testGetBasketNeed_IOException() throws SupporterNotSignedInException, IOException, NeedNotFoundException {
        // Setup
        when(mockNeedDao.getNeed(any())).thenThrow(new IOException());
        userFileDAO.loginUser(testSupporter[0]);

        // Analyze
        assertThrows(IOException.class, () -> {
            userFileDAO.getBasketOrNormalNeed("");
        });
    }

    @Test
    public void testGetBasketNeed_NeedNotFoundException() throws SupporterNotSignedInException, IOException, NeedNotFoundException {
        // Setup
        when(mockNeedDao.getNeed(any())).thenReturn(null);
        userFileDAO.loginUser(testSupporter[0]);

        // Analyze
        assertThrows(NeedNotFoundException.class, () -> {
            userFileDAO.getBasketOrNormalNeed("");
        });
    }

    @Test
    public void testSendOrUpdateMessageToUser() throws IOException {
        // Setup
        NeedMessage message = new NeedMessage("CheeseBell", "testNeed", "testMessage");
        Supporter supporter = testSupporter[0];
        userFileDAO.loginUser(testSupporter[1]);

        // Invoke
        userFileDAO.sendOrUpdateMessageToUser(message, testSupporter[0].getUsername());
        userFileDAO.sendOrUpdateMessageToUser(message, testSupporter[0].getUsername()); // Test send with same need name again

        // Analyze
        assertEquals(message, supporter.getNeedMessages()[1]);
    }

    @Test
    public void testSendOrUpdateMessageToUser_ReceiverNotFound() throws IOException {
        // Setup
        NeedMessage message = new NeedMessage("CheeseBell", "testNeed", "testMessage");
        userFileDAO.loginUser(testSupporter[1]);

        // Invoke
        NeedMessage response = userFileDAO.sendOrUpdateMessageToUser(message, "NOTREALUSER");

        // Analyze
        assertNull(response);
    }

    @Test
    public void testSendOrUpdateMessageToUser_IOException() throws IOException {
        // Setup
        NeedMessage message = new NeedMessage("CheeseBell", "testNeed", "testMessage");
        userFileDAO.loginUser(testSupporter[1]);
        doThrow(new IOException())
                .when(mockObjectMapper)
                .writeValue(any(File.class), any(Supporter[].class));

        // Analyze
        assertThrows(IOException.class, () -> {
            userFileDAO.sendOrUpdateMessageToUser(message, testSupporter[0].getUsername());
        });
    }

    @Test
    public void testGetMessageToUser() throws IOException {
        // Setup
        NeedMessage message = new NeedMessage("CheeseBell", "testNeed", "testMessage");
        userFileDAO.loginUser(testSupporter[1]);
        userFileDAO.sendOrUpdateMessageToUser(message, testSupporter[0].getUsername());

        // Invoke
        NeedMessage response = userFileDAO.getMessageToUser(testSupporter[0].getUsername(), message.getNeedName());

        // Analyze
        assertEquals(message, response);
    }

    @Test
    public void testGetMessageToUser_ReceiverNotFound() throws IOException {
        // Setup
        NeedMessage message = new NeedMessage("CheeseBell", "testNeed", "testMessage");
        userFileDAO.loginUser(testSupporter[1]);
        userFileDAO.sendOrUpdateMessageToUser(message, testSupporter[0].getUsername());

        // Invoke
        NeedMessage response = userFileDAO.getMessageToUser("NOTREALUSER", message.getNeedName());

        // Analyze
        assertNull(response);
    }

    @Test
    public void testGetMessageToUser_NeedNotFound() throws IOException {
        // Setup
        NeedMessage message = new NeedMessage("CheeseBell", "testNeed", "testMessage");
        userFileDAO.loginUser(testSupporter[1]);
        userFileDAO.sendOrUpdateMessageToUser(message, testSupporter[0].getUsername());

        // Invoke
        NeedMessage response = userFileDAO.getMessageToUser(testSupporter[0].getUsername(), "NOTREALNEED");

        // Analyze
        assertNull(response);
    }

    @Test
    public void testGetCurMessages() throws IOException, SupporterNotSignedInException {
        // Setup
        NeedMessage expected = testSupporter[1].getNeedMessages()[0];
        userFileDAO.loginUser(testSupporter[1]);

        // Invoke
        NeedMessage[] response = userFileDAO.getCurMessages();

        // Analyze
        assertEquals(expected, response[0]);
    }

    @Test
    public void testGetCurMessages_SupporterNotSignedIn() throws IOException {
        // Analyze
        assertThrows(SupporterNotSignedInException.class, () -> {
            userFileDAO.getCurMessages();
        });
    }

    @Test
    public void testDeleteCurMessage() throws IOException, SupporterNotSignedInException {
        // Setup
        NeedMessage expected = testSupporter[1].getNeedMessages()[0];
        userFileDAO.loginUser(testSupporter[1]);

        // Invoke
        userFileDAO.deleteCurMessage(expected.getNeedName());

        // Analyze
        assertEquals(0, userFileDAO.getCurMessages().length);
    }

    @Test
    public void testDeleteCurMessage_SupporterNotSignedIn() throws IOException {
        // Analyze
        assertThrows(SupporterNotSignedInException.class, () -> {
            userFileDAO.deleteCurMessage("");
        });
    }

    @Test
    public void testDeleteCurMessage_MessageNotFound() throws IOException, SupporterNotSignedInException {
        // Setup
        userFileDAO.loginUser(testSupporter[1]);

        // Invoke
        userFileDAO.deleteCurMessage("NOTREALNEED");

        // Analyze
        assertEquals(1, userFileDAO.getCurMessages().length);
    }

    @Test
    public void testDeleteCurMessage_IOException() throws IOException, SupporterNotSignedInException {
        // Setup
        NeedMessage expected = testSupporter[1].getNeedMessages()[0];
        userFileDAO.loginUser(testSupporter[1]);
        doThrow(new IOException())
                .when(mockObjectMapper)
                .writeValue(any(File.class), any(Supporter[].class));

        // Analyze
        assertThrows(IOException.class, () -> {
            userFileDAO.deleteCurMessage(expected.getNeedName());
        });
    }
}