package com.ufund.api.ufundapi.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.exceptions.NeedAlreadyInCartException;
import com.ufund.api.ufundapi.exceptions.NeedNotFoundException;
import com.ufund.api.ufundapi.exceptions.SupporterNotSignedInException;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Supporter;
import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.persistence.UserDAO;

/**
 * Handles the REST API requests for the User resource
 * 
 * @author Ethan Hartman
 */

@RestController
@RequestMapping("users")
public class UserController {
    private static final Logger LOG = Logger.getLogger(UserController.class.getName());
    private final UserDAO userDAO;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param userDAO The {@link UserDAO User Data Access Object} to perform CRUD
     *                operations
     *                <br>
     *                This dependency is injected by the Spring Framework
     */
    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Responds to the GET request for a {@linkplain User user} with the given
     * username
     * Will log in the user with the given username, if a user was found
     * 
     * @param username The username used to locate the {@link User user}
     * 
     * @return ResponseEntity with {@link User user} object and HTTP status of OK if
     *         found<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{username}")
    public ResponseEntity<User> loginUser(@PathVariable String username) {
        LOG.info("GET /users/" + username);
        try {
            User user = userDAO.getUser(username);
            if (user != null) {
                userDAO.loginUser(user);
                return new ResponseEntity<User>(user, HttpStatus.OK);
            } else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for logging out
     * Will log out the current user, if there is one
     * 
     * @return ResponseEntity with a boolean value of True and HTTP status of OK
     */
    @GetMapping("/logout")
    public ResponseEntity<Boolean> logout() {
        LOG.info("GET /logout");
        userDAO.logoutCurUser();
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    /**
     * Responds to the GET request for retreiving the current user's basket
     * 
     * @return ResponseEntity with list of {@link Need need} keys and a status of OK
     *         if supporter is signed in<br>
     *         ResponseEntity with HTTP status of FORBIDDEN otherwise
     */
    @GetMapping("/basket")
    public ResponseEntity<String[]> getCurBasket() {
        LOG.info("GET /basket");
        try {
            return new ResponseEntity<String[]>(userDAO.getCurBasket(), HttpStatus.OK);
        } catch (SupporterNotSignedInException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Responds to the POST request for adding a {@linkplain Need need} to the
     * current user's basket based on the {@link Need need}'s name
     * 
     * @param needName The name of the {@link Need need} to add to the basket
     * 
     * @return ResponseEntity with a {@link Need need} and HTTP status of OK if the
     *         {@link Need need} was added to the {@link Supporter supporter's
     *         basket} <br>
     *         ResponseEntity with HTTP status of FORBIDDEN if no supporter is
     *         signed in<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if the {@link Need need}
     *         is not found<br>
     *         ResponseEntity with HTTP status of CONFLICT if the {@link Need need}
     *         is already in the basket<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("/basket/add")
    public ResponseEntity<Need> addToBasket(@RequestBody String needKey) {
        LOG.info("POST /basket/add/" + needKey);
        try {
            return new ResponseEntity<>(userDAO.addNeedToCurBasket(needKey), HttpStatus.OK);
        } catch (SupporterNotSignedInException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (NeedNotFoundException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (NeedAlreadyInCartException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the DELETE request for removing a {@linkplain Need need} from the
     * current user's basket based on the {@link Need need}'s name
     * 
     * @param needName The name of the {@link Need need} to remove from the basket
     * 
     * @return ResponseEntity with a boolean value of True and HTTP status of OK if
     *         {@link Need need} was removed from the {@link Supporter supporter's
     *         basket} <br>
     *         ResponseEntity with HTTP status of FORBIDDEN if no supporter is
     *         signed in<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if the {@link Need need}
     *         is not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("/basket/remove")
    public ResponseEntity<Need> removeFromBasket(@RequestBody String needKey) {
        LOG.info("DELETE /basket/remove/" + needKey);
        try {
            boolean removed = userDAO.removeNeedFromCurBasket(needKey);
            return new ResponseEntity<>(removed ? HttpStatus.OK : HttpStatus.NOT_FOUND);
        } catch (SupporterNotSignedInException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (NeedNotFoundException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
