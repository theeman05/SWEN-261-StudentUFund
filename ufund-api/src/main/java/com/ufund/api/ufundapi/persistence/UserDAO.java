package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import javax.management.openmbean.KeyAlreadyExistsException;

import com.ufund.api.ufundapi.model.BasketNeed;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedMessage;
import com.ufund.api.ufundapi.model.Supporter;
import com.ufund.api.ufundapi.model.User;
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

        /*
         * Updates the quantity of the given {@linkplain Need need} in the current
         * supporter's basket or adds it to the basket with the new quantity.
         * 
         * if quantity is 0, the need will be removed from the basket
         * 
         * @param needKey The key of the {@link Need need} to update in the basket
         * 
         * @param newQuantity The new quantity of the {@link Need need} to update in the
         * basket
         * 
         * @throws IOException if an issue with underlying storage
         * 
         * @throws SupporterNotSignedInException if no supporter is signed in
         * 
         * @throws NeedNotFoundException if the {@link Need need} is not found in the basket
         */
        void updateNeedInCurBasket(String needKey, int newQuantity)
                throws IOException, SupporterNotSignedInException, NeedNotFoundException;

        /**
         * Gets the {@linkplain BasketNeed need} with the given name and quantity in the basket.
         * If the need is not in the basket, the normal need will be returned with a quantity of 0.
         * 
         * @param needName The name of the {@link BasketNeed need} to get
         * 
         * @return The corresponding {@link BasketNeed need}
         * 
         * @throws IOException if an issue with underlying storage
         * 
         * @throws SupporterNotSignedInException if no supporter is signed in
         * 
         * @throws NeedNotFoundException if the {@link BasketNeed need} is not found in the basket nor the normal needs
         */
        public BasketNeed getBasketOrNormalNeed(String needName) 
                throws IOException, SupporterNotSignedInException, NeedNotFoundException;

        /**
         * Gets the needs which are available to add to this {@linkplain Supporter
         * supporter's} basket
         * 
         * @return The {@link Need needs} available to add to the {@link Supporter supporter's}
         *         basket
         * 
         * @throws SupporterNotSignedInException if no {@link Supporter supporter's} is
         *                                       signed in
         * 
         * @throws IOException if an issue with underlying storage
         */
        Need[] getBasketableNeeds() throws SupporterNotSignedInException, IOException;

        /**
         * Gets the current Needs in the current {@linkplain Supporter
         * supporter's} basket
         * 
         * @return The {@link Need needs} in the current {@link Supporter supporter's}
         *         basket
         * 
         * @throws SupporterNotSignedInException if no {@link Supporter supporter's} is
         *                                       signed in
         * 
         * @throws IOException if an issue with underlying storage
         */
        Need[] getCurBasket() throws SupporterNotSignedInException, IOException;

         /**
         * Checks out the current {@linkplain Supporter supporter's} basket
         * 
         * @throws SupporterNotSignedInException if no {@link Supporter supporter's} is
         *                                       signed in
         * 
         * @throws IOException if an issue with underlying storage
         */
        void checkoutCurBasket() throws SupporterNotSignedInException, IOException;

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
         * Gets the current {@linkplain Supporter supporter's} inbox
         * 
         * @return The current {@link Supporter supporter's} inbox
         * 
         * @throws SupporterNotSignedInException if no {@link Supporter supporter's} is
         *                                       signed in
         * 
         * @throws IOException if an issue with underlying storage
         */
        NeedMessage[] getCurMessages() throws SupporterNotSignedInException, IOException;

        /**
         * Sends the given {@linkplain NeedMessage message} to the user with the given username
         * 
         * @param message The {@link NeedMessage message} to send
         * 
         * @param receiverUsername The username of the receiver
         * 
         * @return The {@link NeedMessage message} if successful, null otherwise
         * 
         * @throws IOException if an issue with underlying storage
         */
        NeedMessage sendOrUpdateMessageToUser(NeedMessage message, String receiverUsername) throws IOException;
        
        /**
         * Gets the {@linkplain NeedMessage message} to the user with the given username and need name
         * 
         * @param receiverUsername The username of the receiver
         * 
         * @param needName The name of the {@linkplain Need need}.
         * 
         * @return The {@link NeedMessage message} if successful, null otherwise
         */
        NeedMessage getMessageToUser(String receiverUsername, String needName);
}
