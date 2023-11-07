package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import javax.management.openmbean.KeyAlreadyExistsException;

import com.ufund.api.ufundapi.model.Need;

/**
 * Defines the interface for Need object persistence
 * 
 * @author Ethan Hartman
 */
public interface NeedDAO {
    /**
     * Retrieves all {@linkplain Need needs}
     * 
     * @return An array of {@link Need need} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Need[] getNeeds() throws IOException;

    /**
     * Retrieves all {@linkplain Need needs}
     * 
     * @param supporterUsername The username of the {@link Supporter supporter} whose {@link Need needs} to get
     * 
     * @return An array of {@link Need need} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Need[] getNeeds(String supporterUsername) throws IOException;

    /**
     * Finds all {@linkplain Need needs} whose name contains the given text
     * 
     * @param containsText The text to match against
     * 
     * @return An array of {@link Need needs} whose nemes contains the given text,
     *         may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Need[] findNeeds(String containsText) throws IOException;

    /**
     * Retrieves a {@linkplain Need need} with the given name
     * 
     * @param name The name of the {@link Need need} to get
     * 
     * @return a {@link Need need} object with the matching name
     *         <br>
     *         null if no {@link Need need} with a matching name is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Need getNeed(String name) throws IOException;

    /**
     * Creates and saves a {@linkplain Need need}
     * 
     * @param need {@linkplain Need need} object to be created and saved
     *
     * @return new {@link Need need} if successful, false otherwise
     * 
     * @throws IOException               if an issue with underlying storage
     * 
     * @throws KeyAlreadyExistsException if a {@link Need need} with the same name
     *                                   already exists
     */
    Need createNeed(Need need) throws IOException, KeyAlreadyExistsException;

    /**
     * Updates and saves a {@linkplain Need need}
     * 
     * @param {@link Need need} object to be updated and saved
     * 
     * @return updated {@link Need need} if successful, null if
     *         {@link Need need} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Need updateNeed(Need need) throws IOException;

    /**
     * Deletes a {@linkplain Need need} with the given name
     * 
     * @param name The name of the {@link Need need}
     * 
     * @return true if the {@link Need need} was deleted
     *         <br>
     *         false if Need with the given name does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteNeed(String name) throws IOException;
}