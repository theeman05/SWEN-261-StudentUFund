package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import java.util.Map;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedReceipt;
import com.ufund.api.ufundapi.model.Supporter;

/**
 * Defines the interface for Need Receipt object persistence
 * 
 * @author Ethan Hartman
 */
public interface NeedReceiptDAO {
    /**
     * Retrieves all {@linkplain NeedReceipt needReceipts}
     * 
     * @return An array of {@link NeedReceipt needReceipt} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    NeedReceipt[] getReceipts() throws IOException;

    /**
     * Retrieves all {@linkplain NeedReceipt needs} for the given {@linkplain supporter}'s username
     * 
     * @param supporterUsername The username of the {@link Supporter supporter} whose {@link Need needs} to get
     * 
     * @return An array of {@link Need need} objects, may be empty
     */
    NeedReceipt[] getReceipts(String supporterUsername);

    /**
     * Retrieves a {@linkplain NeedReceipt needReceipt} with the given name and supporter username
     * 
     * @param needName The name of the {@link NeedReceipt needReceipt} to get
     * 
     * @param supporterUsername The username of the {@link Supporter supporter} who funded the {@link Need needReceipt}
     * 
     * @return a {@link NeedReceipt needReceipt} object with the matching name and supporter
     *         <br>
     *         null if no {@link NeedReceipt needReceipt} with a matching name and supporter is found
     */
    NeedReceipt getReceipt(String needName, String supporterUsername);

    /**
     * Creates or updates then saves a {@linkplain NeedReceipt needReceipt}
     * 
     * @param funded {@link Need need} object which was funded
     * 
     * @param supporterUsername The username of the {@link Supporter supporter} who funded the {@link Need need}
     *
     * @return new {@link NeedReceipt needReceipt} if successful, false otherwise
     * 
     * @throws IOException if an issue with underlying storage
     */
    NeedReceipt createOrUpdateReceipt(Need funded, String supporterUsernam) throws IOException;

    /**
     * Gets the total funding of a {@linkplain User user} 
     * 
     * @param supporterUsername
     * 
     * @return Double value of a {@linkplain User user's} total fundings
     * 
     * @throws IOException if an issue with underlying storage
     */
    double getUserFundingSum(String supporterUsername);

    /**
     * Gets all of the total fundings of all {@linkplain User users} 
     * 
     * @return A {@linkplain Map map} of all of the users(key) and their total fundings(value)
     */
    String[] getSortedUserFunding();
}
