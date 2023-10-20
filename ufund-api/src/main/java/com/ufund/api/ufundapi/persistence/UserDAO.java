package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import javax.management.openmbean.KeyAlreadyExistsException;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Supporter;
import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.exceptions.NeedAlreadyInCartException;
import com.ufund.api.ufundapi.exceptions.NeedNotFoundException;
import com.ufund.api.ufundapi.exceptions.SupporterNotSignedInException;

/**
 * Defines the interface for User object persistence
 * 
 * @author Ethan Hartman
 */
public interface UserDAO {
    /**
     * Retrieves all {@linkplain Supporter supporters}
     * 
     * @return An array of {@link Supporter supporter} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Supporter[] getSupporters() throws IOException;

    /**
     * Creates and saves a {@linkplain Supporter supporter}
     * 
     * @param supporter {@link Supporter supporter} object to be created and saved
     *
     * @return new {@link Supporter supporter} if successful, false otherwise
     * 
     * @throws IOException               if an issue with underlying storage
     * 
     * @throws KeyAlreadyExistsException if a {@link Supporter supporter} with the
     *                                   same username already exists
     */
    Supporter createSupporter(Supporter supporter) throws IOException, KeyAlreadyExistsException;

    /**
     * Logs out the current {@linkplain User user} (if there is one).
     */
    void logoutCurUser();

    /**
     * Logs in the {@linkplain User user}.
     * If the {@link User user} is a {@link Supporter supporter}, their basket will
     * be loaded
     * 
     * @param user The {@link User user} to log in
     * 
     * @return true if successful, false if user is not found
     * 
     * @throws IOException if an issue with underlying storage
     */
    boolean loginUser(User user) throws IOException;

    /**
     * Gets the {@linkplain User user} with the given username
     * 
     * @param username The username of the {@link User user} to get
     * 
     * @return The corresponding {@link User user}
     * 
     * @throws IOException if an issue with underlying storage
     */
    User getUser(String username) throws IOException;

    /**
     * Adds the given {@linkplain Need need} to the current supporter's basket
     * 
     * @param needKey The key of the {@link Need need} to add to the basket
     * 
     * @return The associated {@link Need need} if successful, null otherwise
     * 
     * @throws IOException                   if an issue with underlying storage
     * 
     * @throws SupporterNotSignedInException if no supporter is signed in
     * 
     * @throws NeedNotFoundException         if the {@link Need need} is not found
     * 
     * @throws NeedAlreadyInCartException    if the {@link Need need} is already in
     *                                       the basket
     */
    Need addNeedToCurBasket(String needKey)
            throws IOException, SupporterNotSignedInException, NeedNotFoundException, NeedAlreadyInCartException;

    /**
     * Removes the given {@linkplain Need need} from the current supporter's basket
     * 
     * @param needKey The key of the {@link Need need} to remove from the basket
     * 
     * @return true if successful, false if the {@link Need need} is not in the
     *         basket
     * 
     * @throws IOException                   if an issue with underlying storage
     * 
     * @throws SupporterNotSignedInException if no supporter is signed in
     */
    boolean removeNeedFromCurBasket(String needKey)
            throws IOException, SupporterNotSignedInException, NeedNotFoundException;

    /**
     * Gets the current Need's keys in the current {@linkplain Supporter
     * supporter's} basket
     * 
     * @return The {@link Need needs} in the current {@link Supporter supporter's}
     *         basket
     * 
     * @throws SupporterNotSignedInException if no {@link Supporter supporter's} is
     *                                       signed in
     */
    String[] getCurBasket() throws SupporterNotSignedInException;

    /**
     * Gets the current {@linkplain Supporter supporter's} basket
     * 
     * @return The current {@link Supporter supporter's} basket
     * 
     * @throws SupporterNotSignedInException if no {@link Supporter supporter's} is
     *                                       signed in
     */

    User getCurUser() throws SupporterNotSignedInException;

    /**
     * Gets the current {@linkplain Supporter supporter's} basket
     * 
     * @return The current {@link Supporter supporter's} basket
     * 
     * @throws SupporterNotSignedInException if no {@link Supporter supporter's} is
     *                                       signed in
     */
}
