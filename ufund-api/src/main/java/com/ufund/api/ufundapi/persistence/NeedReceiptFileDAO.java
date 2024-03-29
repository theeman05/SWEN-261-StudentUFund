package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedReceipt;

/**
 * Implements the functionality for JSON file-based peristance for Need Receipts
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of
 * this
 * class and injects the instance into other classes as needed
 * 
 * @author Ethan Hartman
 */
@Component
public class NeedReceiptFileDAO implements NeedReceiptDAO {
    private Map<String, Map<String, NeedReceipt>> needReceipts; // Double map of receipts, keyed by username then need name
    private ObjectMapper objectMapper; // Used to serialize/deserialize Java Objects to/from JSON objects
    private String filename; // Filename to read from and write to

    public NeedReceiptFileDAO(@Value("${need_receipts.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load(); // load the receipts from the file
    }

    /**
     * Saves the {@linkplain NeedReceipt receipts} from the map into the file as an
     * array of JSON objects
     * 
     * @return true if the {@link NeedReceipt receipts} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        objectMapper.writeValue(new File(filename), getReceipts());
        return true;
    }

    /**
     * Loads {@linkplain NeedReceipt need receipts} from the JSON file into the map
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        needReceipts = new HashMap<>();

        for (NeedReceipt needReceipt : objectMapper.readValue(new File(filename), NeedReceipt[].class)){
            if (!needReceipts.containsKey(needReceipt.getSupporterUsername()))
                needReceipts.put(needReceipt.getSupporterUsername(), new HashMap<>());
            needReceipts.get(needReceipt.getSupporterUsername()).put(needReceipt.getName(), needReceipt);
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    public NeedReceipt[] getReceipts() {
        ArrayList<NeedReceipt> receiptList = new ArrayList<>();

        for (Map<String, NeedReceipt> userValue : needReceipts.values())
            for (NeedReceipt needReceipt : userValue.values())
                receiptList.add(needReceipt);
        
        return receiptList.toArray(new NeedReceipt[receiptList.size()]);
    }
    
    /**
     * {@inheritDoc}
     */
    public NeedReceipt[] getReceipts(String supporterUsername) {
        if (!needReceipts.containsKey(supporterUsername))
            return new NeedReceipt[0];

        return needReceipts.get(supporterUsername).values().toArray(new NeedReceipt[needReceipts.get(supporterUsername).size()]);
    }

    /**
     * {@inheritDoc}
     */
    public NeedReceipt getReceipt(String needName, String supporterUsername) {
        if (!needReceipts.containsKey(supporterUsername) || !needReceipts.get(supporterUsername).containsKey(needName))
            return null;

        return needReceipts.get(supporterUsername).get(needName);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized NeedReceipt createOrUpdateReceipt(Need need, String supporterUsername) throws IOException {
        if (!needReceipts.containsKey(supporterUsername))
            needReceipts.put(supporterUsername, new HashMap<>());

        NeedReceipt needReceipt = needReceipts.get(supporterUsername).get(need.getName());
        if (needReceipt != null)
            needReceipt.fundMore(need.getCost(), need.getQuantity());
        else{
            needReceipt = new NeedReceipt(supporterUsername, need.getName(), need.getCost(), need.getQuantity());
            needReceipt.setCost(need.getCost() * need.getQuantity());
        }

        needReceipts.get(needReceipt.getSupporterUsername()).put(needReceipt.getName(), needReceipt);
        save();

        return needReceipt;
    }

    /**
     * {@inheritDoc}
     */
    public double getUserFundingSum(String supporterUsername) {
        NeedReceipt[] supporterReceipts = getReceipts(supporterUsername);
        double sum = 0;
        for (NeedReceipt receipt: supporterReceipts) {
            sum += receipt.getCost();
        }
        return sum;
    }

    /**
     * {@inheritDoc}
     */
    public String[] getSortedUserFunding() {
        Map<String, Double> userFunding = new TreeMap<>();
        for (String username : needReceipts.keySet()) {
            Double userTotal = getUserFundingSum(username);
            userFunding.put(username, userTotal);
        }

        // Create a new TreeMap with a comparator for sorting by values in descending order
        TreeMap<String, Double> sortedUserFunding = new TreeMap<>(Comparator.comparingDouble(userFunding::get).reversed());
        sortedUserFunding.putAll(userFunding);

        ArrayList<String> userFundingList = new ArrayList<>();

        for (Map.Entry<String, Double> entry : sortedUserFunding.entrySet()) {
            userFundingList.add(entry.toString());
        }

        String[] userFundingArray = new String[userFundingList.size()];
        for (int i = 0; i < userFundingList.size(); i++) {
            userFundingArray[i] = userFundingList.get(i);
        }

        return userFundingArray;
    }
}
