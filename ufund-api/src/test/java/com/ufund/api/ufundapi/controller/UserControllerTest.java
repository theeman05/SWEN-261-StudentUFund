package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.ufund.api.ufundapi.persistence.UserDAO;
import com.ufund.api.ufundapi.exceptions.NeedAlreadyInCartException;
import com.ufund.api.ufundapi.exceptions.NeedNotFoundException;
import com.ufund.api.ufundapi.exceptions.SupporterNotSignedInException;
import com.ufund.api.ufundapi.model.Need;
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
        String[] expected_basket = {"TestNeed1", "TestNeed2"};
        when(mockUserDAO.getCurBasket()).thenReturn(expected_basket);

        // Invoke
        ResponseEntity<String[]> response = userController.getCurBasket();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected_basket, response.getBody());
    }

    @Test
    public void testGetBasketSupporterNotSignedIn() throws IOException, SupporterNotSignedInException {
        // Setup
        doThrow(new SupporterNotSignedInException()).when(mockUserDAO).getCurBasket();

        // Invoke
        ResponseEntity<String[]> response = userController.getCurBasket();
        
        // Analyze
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testAddToBasket() throws IOException, SupporterNotSignedInException, NeedNotFoundException, NeedAlreadyInCartException {
        // Setup
        Need expected_need = new Need("TestNeed", 1, 1);
        when(mockUserDAO.addNeedToCurBasket(expected_need.getName())).thenReturn(expected_need);

        // Invoke
        ResponseEntity<Need> response = userController.addToBasket(expected_need.getName());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected_need, response.getBody());
    }

    @Test
    public void testAddToBasketNotSignedIn() throws IOException, SupporterNotSignedInException, NeedNotFoundException, NeedAlreadyInCartException {
        // Setup
        String need_name = "TestNeed";
        doThrow(new SupporterNotSignedInException()).when(mockUserDAO).addNeedToCurBasket(need_name);

        // Invoke
        ResponseEntity<Need> response = userController.addToBasket(need_name);

        // Analyze
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testAddToBasketNeedNotFound() throws IOException, SupporterNotSignedInException, NeedNotFoundException, NeedAlreadyInCartException {
        // Setup
        String need_name = "TestNeed";
        doThrow(new NeedNotFoundException(need_name)).when(mockUserDAO).addNeedToCurBasket(need_name);

        // Invoke
        ResponseEntity<Need> response = userController.addToBasket(need_name);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddToBasketAlreadyInCart() throws IOException, SupporterNotSignedInException, NeedNotFoundException, NeedAlreadyInCartException {
        // Setup
        String need_name = "TestNeed";
        doThrow(new NeedAlreadyInCartException(need_name)).when(mockUserDAO).addNeedToCurBasket(need_name);

        // Invoke
        ResponseEntity<Need> response = userController.addToBasket(need_name);

        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testAddToBasketIOException() throws IOException, SupporterNotSignedInException, NeedNotFoundException, NeedAlreadyInCartException {
    // Setup
    String need_name = "TestNeed";
    doThrow(new IOException()).when(mockUserDAO).addNeedToCurBasket(need_name);

    // Invoke
    ResponseEntity<Need> response = userController.addToBasket(need_name);

    // Analyze
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testRemoveNeedFromBasket() throws IOException, SupporterNotSignedInException, NeedNotFoundException {
        // Invoke
        ResponseEntity<Void> response = userController.removeFromBasket(anyString());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRemoveNeedFromBasketNotSignedIn() throws IOException, SupporterNotSignedInException, NeedNotFoundException {
        // Setup
        String need_name = "TestNeed";
        doThrow(new SupporterNotSignedInException()).when(mockUserDAO).removeNeedFromCurBasket(need_name);

        // Invoke
        ResponseEntity<Void> response = userController.removeFromBasket(need_name);

        // Analyze
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testRemoveNeedFromBasketNeedNotFound() throws IOException, SupporterNotSignedInException, NeedNotFoundException {
        // Setup
        String need_name = "TestNeed";
        doThrow(new NeedNotFoundException(need_name)).when(mockUserDAO).removeNeedFromCurBasket(need_name);

        // Invoke
        ResponseEntity<Void> response = userController.removeFromBasket(need_name);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testRemoveNeedFromBasketIOException() throws IOException, SupporterNotSignedInException, NeedNotFoundException {
        // Setup
        String need_name = "TestNeed";
        doThrow(new IOException()).when(mockUserDAO).removeNeedFromCurBasket(need_name);

        // Invoke
        ResponseEntity<Void> response = userController.removeFromBasket(need_name);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}