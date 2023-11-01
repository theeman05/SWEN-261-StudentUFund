package com.ufund.api.ufundapi.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.exceptions.NeedNotFoundException;
import com.ufund.api.ufundapi.exceptions.SupporterNotSignedInException;
import com.ufund.api.ufundapi.model.BasketNeed;
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
    public ResponseEntity<Need[]> getCurBasket() {
        LOG.info("GET /basket");
        try {
            return new ResponseEntity<Need[]>(userDAO.getCurBasket(), HttpStatus.OK);
        } catch (SupporterNotSignedInException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the Get request for getting a {@linkplain Need need} in the current user's basket
     * 
     * @param needName The name of the {@link Need need} in the basket
     * 
     * @return ResponseEntity with a {@link Need need} and HTTP status of OK if the
     *         {@link Need need} was found in the system <br>
     *         ResponseEntity with HTTP status of FORBIDDEN if no supporter is
     *         signed in<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if the {@link Need need}
     *         is not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/basket/{needKey}")
    public ResponseEntity<BasketNeed> getBasketNeed(@PathVariable String needKey){
        LOG.info("GET /basket/" + needKey);
        try {
            return new ResponseEntity<BasketNeed>(userDAO.getBasketOrNormalNeed(needKey), HttpStatus.OK);
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

    /**
     * Responds to the PUT request for updating a {@linkplain Need need} in the current user's basket
     * 
     * @param needName The name of the {@link Need need} to add to the basket
     * 
     * @param quantity The quantity of the {@link Need need} to update the basket to
     * 
     * @return ResponseEntity with a {@link Need need} and HTTP status of OK if the
     *         {@link Need need} was added to the {@link Supporter supporter's
     *         basket} <br>
     *         ResponseEntity with HTTP status of FORBIDDEN if no supporter is
     *         signed in<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if the {@link Need need}
     *         is not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/basket")
    public ResponseEntity<Void> updateNeedInBasket(@RequestBody Need need) {
        LOG.info("PUT update need in basket: " + need);
        try {
            userDAO.updateNeedInCurBasket(need.getName(), need.getQuantity());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (SupporterNotSignedInException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (NeedNotFoundException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (IOException e) {
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
     * @return ResponseEntity with an HTTP status of OK if
     *         {@link Need need} was removed from the {@link Supporter supporter's
     *         basket} <br>
     *         ResponseEntity with HTTP status of FORBIDDEN if no supporter is
     *         signed in<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if the {@link Need need}
     *         is not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/basket/{needKey}")
    public ResponseEntity<Void> removeFromBasket(@PathVariable String needKey) {
        LOG.info("DELETE /basket/" + needKey);
        try {
            userDAO.updateNeedInCurBasket(needKey, 0);
            return new ResponseEntity<>(HttpStatus.OK);
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

    /**
     * Responds to the GET request for checking out the current user's basket
     * 
     * @return ResponseEntity with an HTTP status of OK if the basket was checked
     *         out<br>
     *         ResponseEntity with HTTP status of FORBIDDEN if no supporter is
     *         signed in<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/checkout")
    public ResponseEntity<Void> checkoutBasket() {
        LOG.info("GET /checkout");
        try {
            userDAO.checkoutCurBasket();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (SupporterNotSignedInException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for getting the {@link Need needs} which are
     * available to add to the current user's basket
     * 
     * @return ResponseEntity with list of {@link Need need} objects and a status of
     *         OK if supporter is signed in<br>
     *         ResponseEntity with HTTP status of FORBIDDEN otherwise
     */
    @GetMapping("/basketable")
    public ResponseEntity<Need[]> getBasketableNeeds() {
        LOG.info("GET /basketable");
        try {
            return new ResponseEntity<Need[]>(userDAO.getBasketableNeeds(), HttpStatus.OK);
        } catch (SupporterNotSignedInException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
