package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import javax.management.openmbean.KeyAlreadyExistsException;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Supporter;
import com.ufund.api.ufundapi.model.User;

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
     * @param supporter {@linkplain Supporter supporter} object to be created and saved
     *
     * @return new {@link Supporter supporter} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     * 
     * @throws KeyAlreadyExistsException if a {@link Supporter supporter} with the same username already exists
     */
    Supporter createSupporter(Supporter supporter) throws IOException, KeyAlreadyExistsException;

    /**
     * Logs out the current {@linkplain User user} (if there is one).
     */
    void logoutCurUser();

    /**
     * Logs in the {@linkplain User user}.
     * If the {@linkplain User user} is a {@linkplain Supporter supporter}, their basket will be loaded
     * 
     * @param user The {@linkplain User user} to log in
     * 
     * @return true if successful, false if user is not found
     */
    boolean loginUser(User user);

    /**
     * Adds the given need to the current supporter's basket
     * 
     * @param need The {@linkPlain Need need} to add to the basket
     * 
     * @return true if successful, false if the need is already in the basket
     * 
     * @throws IOException if an issue with underlying storage
     * 
     * @throws SupporterNotSignedInException if no supporter is signed in
     */
    boolean addNeedToCurBasket(Need need) throws IOException, SupporterNotSignedInException;

    /**
     * Removes the given need from the current supporter's basket
     * 
     * @param need The {@linkPlain Need need} to remove from the basket
     * 
     * @return true if successful, false if the need is not in the basket
     * 
     * @throws IOException if an issue with underlying storage
     * 
     * @throws SupporterNotSignedInException if no supporter is signed in
     */
    boolean removeNeedFromCurBasket(Need need) throws IOException, SupporterNotSignedInException;

    /**
     * Gets the current supporter's basket
     * 
     * @return The {@link Need needs} in the current supporter's basket
     * 
     * @throws SupporterNotSignedInException if no supporter is signed in
     */
    Need[] getCurBasket() throws SupporterNotSignedInException;
}
