package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.ufund.api.ufundapi.persistence.UserDAO;
import com.ufund.api.ufundapi.exceptions.NeedNotFoundException;
import com.ufund.api.ufundapi.exceptions.SupporterNotSignedInException;
import com.ufund.api.ufundapi.model.BasketNeed;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedMessage;
import com.ufund.api.ufundapi.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the User Controller class
 * 
 * @author Ethan Hartman
 */
@Tag("Controller-tier")
public class UserControllerTest {
    private UserController userController;
    private UserDAO mockUserDAO;

    /**
     * Before each test, create a new UserController object and inject a mock User DAO
     */
    @BeforeEach
    public void setupUserController() {
        mockUserDAO = mock(UserDAO.class);
        userController = new UserController(mockUserDAO);
    }

    @Test
    public void testLoginUser() throws IOException {
        // Setup
        User user = new User("TestUser");
        // When the same username is passed in, our mock DAO will return the User object
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);

        // Invoke
        ResponseEntity<User> response = userController.loginUser(user.getUsername());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testUserNotFound() throws IOException {
        // Setup
        User user = new User("TestUserNotFound");

        // Invoke
        ResponseEntity<User> response = userController.loginUser(user.getUsername());

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUserStorageIssue() throws IOException, SupporterNotSignedInException {
        // Setup
        String username = "TestUser";
        doThrow(new IOException()).when(mockUserDAO).getUser(username);
        
        // Invoke
        ResponseEntity<User> response = userController.loginUser(username);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testLogoutUser() throws IOException {
        // Setup
        User user = new User("TestUser");
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);
        userController.loginUser(user.getUsername());
        boolean expected_response = true;

        // Invoke
        ResponseEntity<Boolean> response = userController.logout();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected_response, response.getBody());
    }

    @Test
    public void testGetBasket() throws IOException, SupporterNotSignedInException {
        // Setup
        Need[] expected_basket = {new Need("TestNeed1", 1, 1), new Need("TestNeed2", 2, 2)};
        when(mockUserDAO.getCurBasket()).thenReturn(expected_basket);

        // Invoke
        ResponseEntity<Need[]> response = userController.getCurBasket();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected_basket, response.getBody());
    }

    @Test
    public void testGetBasketSupporterNotSignedIn() throws IOException, SupporterNotSignedInException {
        // Setup
        doThrow(new SupporterNotSignedInException()).when(mockUserDAO).getCurBasket();

        // Invoke
        ResponseEntity<Need[]> response = userController.getCurBasket();
        
        // Analyze
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testGetBasketIOException() throws IOException, SupporterNotSignedInException {
        // Setup
        doThrow(new IOException()).when(mockUserDAO).getCurBasket();

        // Invoke
        ResponseEntity<Need[]> response = userController.getCurBasket();
        
        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testRemoveNeedFromBasket() throws IOException, SupporterNotSignedInException, NeedNotFoundException {
        // Invoke
        ResponseEntity<Void> response = userController.removeFromBasket("Chee");

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRemoveNeedFromBasketNotSignedIn() throws IOException, SupporterNotSignedInException, NeedNotFoundException {
        // Setup
        String need_name = "TestNeed";
        doThrow(new SupporterNotSignedInException()).when(mockUserDAO).updateNeedInCurBasket(need_name, 0);

        // Invoke
        ResponseEntity<Void> response = userController.removeFromBasket(need_name);

        // Analyze
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testRemoveNeedFromBasketNeedNotFound() throws IOException, SupporterNotSignedInException, NeedNotFoundException {
        // Setup
        String need_name = "TestNeed";
        doThrow(new NeedNotFoundException(need_name)).when(mockUserDAO).updateNeedInCurBasket(need_name, 0);

        // Invoke
        ResponseEntity<Void> response = userController.removeFromBasket(need_name);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testRemoveNeedFromBasketIOException() throws IOException, SupporterNotSignedInException, NeedNotFoundException {
        // Setup
        String need_name = "TestNeed";
        doThrow(new IOException()).when(mockUserDAO).updateNeedInCurBasket(need_name, 0);

        // Invoke
        ResponseEntity<Void> response = userController.removeFromBasket(need_name);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateNeedInBasket() throws IOException, SupporterNotSignedInException, NeedNotFoundException {
        // Setup
        Need test_need = new Need("TestNeed", 1, 1);

        // Invoke
        ResponseEntity<Void> response = userController.updateNeedInBasket(test_need);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateNeedInBasketNotSignedIn() throws IOException, SupporterNotSignedInException, NeedNotFoundException {
        // Setup
        Need test_need = new Need("TestNeed", 1, 1);
        doThrow(new SupporterNotSignedInException()).when(mockUserDAO).updateNeedInCurBasket(test_need.getName(), test_need.getQuantity());

        // Invoke
        ResponseEntity<Void> response = userController.updateNeedInBasket(test_need);

        // Analyze
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testUpdateNeedInBasketNeedNotFound() throws IOException, SupporterNotSignedInException, NeedNotFoundException {
        // Setup
        Need test_need = new Need("TestNeed", 1, 1);
        doThrow(new NeedNotFoundException(test_need.getName())).when(mockUserDAO).updateNeedInCurBasket(test_need.getName(), test_need.getQuantity());

        // Invoke
        ResponseEntity<Void> response = userController.updateNeedInBasket(test_need);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateNeedInBasketIOException() throws IOException, SupporterNotSignedInException, NeedNotFoundException {
        // Setup
        Need test_need = new Need("TestNeed", 1, 1);
        doThrow(new IOException()).when(mockUserDAO).updateNeedInCurBasket(test_need.getName(), test_need.getQuantity());

        // Invoke
        ResponseEntity<Void> response = userController.updateNeedInBasket(test_need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCheckoutBasket() throws IOException, SupporterNotSignedInException {
        // Invoke
        ResponseEntity<Void> response = userController.checkoutBasket();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testCheckoutBasketNotSignedIn() throws IOException, SupporterNotSignedInException {
        // Setup
        doThrow(new SupporterNotSignedInException()).when(mockUserDAO).checkoutCurBasket();

        // Invoke
        ResponseEntity<Void> response = userController.checkoutBasket();

        // Analyze
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testCheckoutBasketIOException() throws IOException, SupporterNotSignedInException {
        // Setup
        doThrow(new IOException()).when(mockUserDAO).checkoutCurBasket();

        // Invoke
        ResponseEntity<Void> response = userController.checkoutBasket();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetBasketable() throws IOException, SupporterNotSignedInException {
        // Invoke
        ResponseEntity<Need[]> response = userController.getBasketableNeeds();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetBasketableNotSignedIn() throws IOException, SupporterNotSignedInException {
        // Setup
        doThrow(new SupporterNotSignedInException()).when(mockUserDAO).getBasketableNeeds();

        // Invoke
        ResponseEntity<Need[]> response = userController.getBasketableNeeds();

        // Analyze
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testGetBasketableIOException() throws IOException, SupporterNotSignedInException {
        // Setup
        doThrow(new IOException()).when(mockUserDAO).getBasketableNeeds();

        // Invoke
        ResponseEntity<Need[]> response = userController.getBasketableNeeds();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetBasketNeed() throws IOException, SupporterNotSignedInException, NeedNotFoundException {
        // Setup
        BasketNeed expected_need = new BasketNeed("TestNeed", 1, 1, 15);
        when(mockUserDAO.getBasketOrNormalNeed(expected_need.getName())).thenReturn(expected_need);

        // Invoke
        ResponseEntity<BasketNeed> response = userController.getBasketNeed(expected_need.getName());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected_need, response.getBody());
    }

    @Test
    public void testGetBasketNeed_Failure() throws IOException, SupporterNotSignedInException, NeedNotFoundException {
        // Setup
        when(mockUserDAO.getBasketOrNormalNeed(any())).thenThrow(new NeedNotFoundException(""), new IOException(), new SupporterNotSignedInException());
        
        // Invoke
        ResponseEntity<BasketNeed> response1 = userController.getBasketNeed("");
        ResponseEntity<BasketNeed> response2 = userController.getBasketNeed("");
        ResponseEntity<BasketNeed> response3 = userController.getBasketNeed("");

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response1.getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response2.getStatusCode());
        assertEquals(HttpStatus.FORBIDDEN, response3.getStatusCode());
    }

    @Test
    public void testGetCurUser() throws SupporterNotSignedInException {
        // Setup
        User expected_user = new User("TestUser");
        when(mockUserDAO.getCurUser()).thenReturn(expected_user);

        // Invoke
        ResponseEntity<User> response = userController.getCurUser();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected_user, response.getBody());
    }

    @Test
    public void testGetCurUser_Failure() throws SupporterNotSignedInException {
        // Setup
        when(mockUserDAO.getCurUser()).thenThrow(new SupporterNotSignedInException());

        // Invoke
        ResponseEntity<User> response = userController.getCurUser();

        // Analyze
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
    
    @Test
    public void testGetInbox() {
        // Invoke
        ResponseEntity<NeedMessage[]> response = userController.getInbox();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetInbox_Failure() throws SupporterNotSignedInException {
        // Setup
        when(mockUserDAO.getCurMessages()).thenThrow(new SupporterNotSignedInException());

        // Invoke
        ResponseEntity<NeedMessage[]> response = userController.getInbox();

        // Analyze
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testSendMessage() {
        // Setup
        NeedMessage test_message = new NeedMessage("Sender", "TestNeed", "TestMessage");

        // Invoke
        ResponseEntity<Void> response = userController.sendMessage(test_message);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testSendMessage_Failure() throws IOException{
        // Setup
        NeedMessage test_message = new NeedMessage("admin", "TestNeed", "TestMessage");
        when(mockUserDAO.sendOrUpdateMessageToUser(any(NeedMessage.class), anyString())).thenThrow(new IOException());

        // Invoke
        ResponseEntity<Void> response = userController.sendMessage(test_message);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteMessage() {
        // Invoke
        ResponseEntity<Void> response = userController.deleteMessage("TestMessage");

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteMessage_Failure() throws IOException, SupporterNotSignedInException{
        // Setup
        doThrow(new IOException(), new SupporterNotSignedInException()).when(mockUserDAO).deleteCurMessage("TestMessage");

        // Invoke
        ResponseEntity<Void> response = userController.deleteMessage("TestMessage");
        ResponseEntity<Void> response1 = userController.deleteMessage("TestMessage");

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(HttpStatus.FORBIDDEN, response1.getStatusCode());
    }

    @Test
    public void testGetMessage() {
        // Setup
        NeedMessage expected_message = new NeedMessage("Sender", "TestNeed", "TestMessage");
        when(mockUserDAO.getMessageToUser("receiver", "TestNeed")).thenReturn(expected_message);

        // Invoke
        ResponseEntity<NeedMessage> response = userController.getMessage("receiver", "TestNeed");

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected_message, response.getBody());
    }
}