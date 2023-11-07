package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

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
     * Retrieves a {@linkplain NeedReceipt needReceipt} with the given name and supporter username
     * 
     * @param needName The name of the {@link NeedReceipt needReceipt} to get
     * 
     * @param supporterUsername The username of the {@link Supporter supporter} who funded the {@link Need needReceipt}
     * 
     * @return a {@link NeedReceipt needReceipt} object with the matching name and supporter
     *         <br>
     *         null if no {@link NeedReceipt needReceipt} with a matching name and supporter is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    NeedReceipt getReceipt(String needName, String supporterUsername) throws IOException;

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
}
