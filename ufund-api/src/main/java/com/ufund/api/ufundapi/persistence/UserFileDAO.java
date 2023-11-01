package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.management.openmbean.KeyAlreadyExistsException;

import com.ufund.api.ufundapi.exceptions.NeedNotFoundException;
import com.ufund.api.ufundapi.exceptions.SupporterNotSignedInException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.BasketNeed;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Supporter;
import com.ufund.api.ufundapi.model.User;

/**
 * Implements the functionality for JSON file-based peristance for Users
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of
 * this
 * class and injects the instance into other classes as needed
 * 
 * @author Ethan Hartman
 */
@Component
public class UserFileDAO implements UserDAO {
    private static String SupporterUsernameExistsException = "Supporter with the username '%s' already exists";

    private Map<String, Supporter> supporters; // Map of supporters, keyed by username
    private ObjectMapper objectMapper; // Used to serialize/deserialize Java Objects to/from JSON objects
    private String filename; // Filename to read from and write to

    private User curUser; // The current user logged in
    Map<String, Need> supporterBasket; // The current supporter's basket of needs

    private NeedDAO needDao;

    /**
     * Creates a Supporter File Data Access Object
     * 
     * @param filename     Filename to read from and write to
     * 
     * @param objectMapper Provides JSON Object to/from Java Object serialization
     *                     and deserialization
     * 
     * @param needDao      The {@link NeedDAO Need Data Access Object} to perform
     *                     CRUD operations
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    @Autowired
    public UserFileDAO(@Value("${supporters.file}") String filename, ObjectMapper objectMapper, NeedDAO needDao)
            throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        this.needDao = needDao;
        load(); // load the supporters from the file
    }

    /**
     * Saves the {@linkplain Supporter supporters} from the map into the file as an
     * array of JSON objects
     * 
     * @return true if the {@link Supporter supporters} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        objectMapper.writeValue(new File(filename), getSupporters());
        return true;
    }

    public User getCurUser() {
        return curUser;
    }

    /**
     * Loads {@linkplain Supporter supporters} from the JSON file into the map
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        supporters = new HashMap<>();

        // Deserializes the JSON objects from the file into an array of supporters
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        for (Supporter supporter : objectMapper.readValue(new File(filename), Supporter[].class))
            supporters.put(supporter.getUsername(), supporter);

        return true;
    }

    /**
     * Updates and saves the logged in {@linkplain Supporter supporter}
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    private void updateCurSupporter() throws IOException {
        Supporter supporter = (Supporter) curUser;
        synchronized (supporters) {
            supporter.setFundingBasket(supporterBasket.values().toArray(new Need[supporterBasket.size()]));
            save(); // may throw an IOException
        }
    }

    /**
     * {@inheritDoc}
     */
    public Supporter[] getSupporters() throws IOException {
        return supporters.values().toArray(new Supporter[supporters.size()]);
    }

    /**
     * {@inheritDoc}
     */
    public Supporter createSupporter(Supporter supporter) throws IOException, KeyAlreadyExistsException {
        synchronized (supporters) {
            if (supporters.containsKey(supporter.getUsername()) || supporter.isAdmin())
                throw new KeyAlreadyExistsException(
                        String.format(SupporterUsernameExistsException, supporter.getUsername()));
            supporters.put(supporter.getUsername(), supporter);
            save(); // may throw an IOException
            return supporter;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void logoutCurUser() {
        supporterBasket = null;
        curUser = null;
    }


    /**
     * Gets the given supporter's funding basket and returns it, while filtering deleted needs, and updated quantities
     * If a need is found to have more quantity than the cupboard, the quantity is updated to the cupboard's quantity
     * If a need is no longer in the cupboard, it is removed from the basket
     * 
     * @param supporter The supporter to get the basket for
     * 
     * @return The supporter's potentially updated basket
     */
    private synchronized Need[] getAndUpdateSupporterBasket(Supporter supporter) throws IOException{
        Need matchedNeed;
        Need[] updatedBasket;
        supporterBasket = new HashMap<>();
        boolean removedNeed = false;
        for (Need need : supporter.getFundingBasket()) {
            matchedNeed = needDao.getNeed(need.getName());
            if (matchedNeed != null){
                // Set quantity to be based on the avaliable quantity in the cupboard
                need.setQuantity(Math.min(matchedNeed.getQuantity(), need.getQuantity()));
                supporterBasket.put(need.getName(), need);
            }else
                removedNeed = true;
        }

        updatedBasket = supporterBasket.values().toArray(new Need[supporterBasket.size()]);
        if (removedNeed){
            supporter.setFundingBasket(updatedBasket);
            save();
        }

        return updatedBasket;
    }

    /**
     * {@inheritDoc}
     */
    public boolean loginUser(User user) throws IOException {
        if (curUser == user) {
            return true;
        }
        logoutCurUser();
        if (!user.isAdmin()) {
            // If the user is in the system
            if (supporters.containsKey(user.getUsername())) 
                getAndUpdateSupporterBasket((Supporter) user);
            else
                return false;
        }
        curUser = user;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public User getUser(String username) throws IOException {
        if (User.ADMIN.getUsername().equals(username))
            return User.ADMIN;
        return supporters.get(username);
    }

    /**
     * {@inheritDoc}
     */
    public void updateNeedInCurBasket(String needKey, int newQuantity)
            throws IOException, SupporterNotSignedInException, NeedNotFoundException {
        if (supporterBasket == null)
            throw new SupporterNotSignedInException();
        
        Need locatedNeed = needDao.getNeed(needKey);
        if (locatedNeed == null)
            throw new NeedNotFoundException(needKey);

        Need basketNeed = !supporterBasket.containsKey(needKey) ? new Need(locatedNeed.getName(), locatedNeed.getCost(), 0) : supporterBasket.get(needKey);;
        basketNeed.setQuantity(newQuantity);

        if (basketNeed.getQuantity() <= 0)
            supporterBasket.remove(needKey);
        else
            supporterBasket.put(needKey, basketNeed);

        synchronized (supporterBasket) {
            updateCurSupporter();
        }
    }


    /**
     * {@inheritDoc}
     */
    public Need[] getCurBasket() throws SupporterNotSignedInException, IOException {
        if (supporterBasket == null)
            throw new SupporterNotSignedInException();
        return getAndUpdateSupporterBasket((Supporter) curUser);
    }

    /**
     * {@inheritDoc}
     */
    public void checkoutCurBasket() throws SupporterNotSignedInException, IOException {
        if (supporterBasket == null)
            throw new SupporterNotSignedInException();

        synchronized (supporterBasket) {
            // Update the need from the list of needs since it's been funded
            Need matchedNeed;
            for (Need need : supporterBasket.values()){
                matchedNeed = needDao.getNeed(need.getName());
                // If we find a matching need, update the quantity. If quantity is 0 or below, delete the need
                if (matchedNeed != null){
                    matchedNeed.setQuantity(matchedNeed.getQuantity() - need.getQuantity());
                    if (matchedNeed.getQuantity() > 0)
                        needDao.updateNeed(matchedNeed);
                    else
                        needDao.deleteNeed(matchedNeed.getName());
                }
            }
            
            supporterBasket.clear();
            updateCurSupporter();
        }
    }

    /**
     * {@inheritDoc}
     */
    public BasketNeed getBasketOrNormalNeed(String needName) throws IOException, SupporterNotSignedInException, NeedNotFoundException {
        if (supporterBasket == null)
            throw new SupporterNotSignedInException();

        Need needMatch = needDao.getNeed(needName);
        if (needMatch == null)
            throw new NeedNotFoundException(needName);
        
        // If the need is in the basket, the quantity will be how many are in there, otherwise 0
        return new BasketNeed(needMatch.getName(), needMatch.getCost(), Math.min(supporterBasket.containsKey(needName) ? supporterBasket.get(needName).getQuantity() : 0, needMatch.getQuantity()), needMatch.getQuantity());
    }

    /**
     * {@inheritDoc}
     */
    public Need[] getBasketableNeeds() throws IOException, SupporterNotSignedInException {
        if (supporterBasket == null)
            throw new SupporterNotSignedInException();

        ArrayList<Need> basketable = new ArrayList<>();
        Need basketNeed;
        for (Need need : needDao.getNeeds()){
            basketNeed = supporterBasket.get(need.getName());
            if (basketNeed == null)
                basketable.add(need);
            else if (need.getQuantity() > basketNeed.getQuantity()) {
                // If the need is already in the basket, check if the quantity is less than the cupboard quantity
                // If it is, add an updated need with remaining quantity
                basketable.add(new Need(need.getName(), need.getCost(), need.getQuantity() - basketNeed.getQuantity()));
            }
        }

        return basketable.toArray(new Need[basketable.size()]);
    }
}
