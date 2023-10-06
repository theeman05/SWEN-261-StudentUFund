package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.management.openmbean.KeyAlreadyExistsException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Supporter;
import com.ufund.api.ufundapi.model.User;

/**
 * Implements the functionality for JSON file-based peristance for Users
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author Ethan Hartman
 */
@Component
public class UserFileDAO implements UserDAO {
    private static String SupporterUsernameExistsException = "Supporter with the username '%s' already exists";

    private Map<String, Supporter> supporters;  // Map of supporters, keyed by username
    private ObjectMapper objectMapper;  // Used to serialize/deserialize Java Objects to/from JSON objects
    private String filename;    // Filename to read from and write to

    private User curUser; // The current user logged in
    private Map<String, Need> supporterBasket; // The current supporter's basket of needs 

    /**
     * Creates a Supporter File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public UserFileDAO(@Value("${supporters.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the supporters from the file
    }
    
    /**
     * Saves the {@linkplain Supporter supporters} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link Supporter supporters} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        objectMapper.writeValue(new File(filename), getSupporters());
        return true;
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
        synchronized(supporters) {
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
        synchronized(supporters) {
            if (supporters.containsKey(supporter.getUsername()) || supporter.isAdmin())
                throw new KeyAlreadyExistsException(String.format(SupporterUsernameExistsException, supporter.getUsername()));
            supporters.put(supporter.getUsername(), supporter);
            save(); // may throw an IOException
            return supporter;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void logoutCurUser(){
        supporterBasket = null;
        curUser = null;
    }

    /**
     * {@inheritDoc}
     */
    public boolean loginUser(User user) {
        if (curUser == user)
            return true;
        logoutCurUser();
        if (!user.isAdmin()){
            // Ensure the user is in the system.
            if (supporters.containsKey(user.getUsername())) {
                supporterBasket = new HashMap<>();
                for (Need need : ((Supporter) user).getFundingBasket())
                    supporterBasket.put(need.getName(), need);
            }else
                return false;
        }
        curUser = user;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public User getUser(String username) throws IOException{
        if (User.ADMIN.getUsername().equals(username))
            return User.ADMIN;
        return supporters.get(username);
    }

    /**
     * {@inheritDoc}
     */
    public boolean addNeedToCurBasket(Need need) throws IOException, SupporterNotSignedInException{
        if (supporterBasket == null)
            throw new SupporterNotSignedInException();

        if (supporterBasket.containsKey(need.getName()))
            return false;

        synchronized(supporterBasket){
            supporterBasket.put(need.getName(), need);
            updateCurSupporter();
            return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean removeNeedFromCurBasket(Need need) throws IOException, SupporterNotSignedInException{
        if (supporterBasket == null)
            throw new SupporterNotSignedInException();

        if (!supporterBasket.containsKey(need.getName()))
            return false;

        synchronized(supporterBasket){
            supporterBasket.remove(need.getName());
            updateCurSupporter();
            return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    public Need[] getCurBasket() throws SupporterNotSignedInException{
        if (supporterBasket == null)
            throw new SupporterNotSignedInException();
        return supporterBasket.values().toArray(new Need[supporterBasket.size()]);
    }
}
