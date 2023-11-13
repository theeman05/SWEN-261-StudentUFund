package com.ufund.api.ufundapi.controller;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedReceipt;
import com.ufund.api.ufundapi.model.Supporter;
import com.ufund.api.ufundapi.persistence.NeedReceiptDAO;

/**
 * Handles the REST API requests for the Need Receipt resource
 * 
 * @author Ethan Hartman
 */
@RestController
@RequestMapping("receipts")
public class NeedReceiptController {
    private static final Logger LOG = Logger.getLogger(NeedReceiptController.class.getName());
    private final NeedReceiptDAO needReceiptDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param needReceiptDao The {@link NeedReceiptDAO Need Receipt Data Access Object} to perform CRUD
     *                operations
     *                <br>
     *                This dependency is injected by the Spring Framework
     */
    public NeedReceiptController(NeedReceiptDAO needReceiptDao) {
        this.needReceiptDao = needReceiptDao;
    }
    
    /**
     * Responds to the GET request for a {@linkplain NeedReceipt needReceipt} for the given name and supporter username
     * 
     * @param name The {@linkplain Need need} name used to locate the {@link NeedReceipt needReceipt}
     * 
     * @param supporterUsername The username of the {@link Supporter supporter} who funded the {@link Need needReceipt}
     * 
     * @return ResponseEntity with {@link NeedReceipt needReceipt} object and HTTP status of OK if
     *         found<br>
     *         ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{supporterUsername}/{name}")
    public ResponseEntity<NeedReceipt> getNeedReceipt(@PathVariable String supporterUsername, @PathVariable String name) {
        LOG.info("GET /receipts/" + supporterUsername + "/" + name);
        try {
            NeedReceipt needReceipt = needReceiptDao.getReceipt(name, supporterUsername);
            if (needReceipt != null)
                return new ResponseEntity<>(needReceipt, HttpStatus.OK);
            else 
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request to retrieve all {@linkplain NeedReceipt needReceipts}
     * 
     * @return ResponseEntity with {@link NeedReceipt needReceipt} objects and HTTP status of OK 
     *          <br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<NeedReceipt[]> getNeedReceipts() {
        LOG.info("GET /receipts");
        try {
            return new ResponseEntity<>(needReceiptDao.getReceipts(), HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request to retrieve all {@linkplain NeedReceipt needReceipts} of the given supporter
     * 
     * @return ResponseEntity with {@link NeedReceipt needReceipt} objects and HTTP status of OK 
     *          <br>
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{supporterUsername}")
    public ResponseEntity<NeedReceipt[]> getNeedReceipts(@PathVariable String supporterUsername) {
        LOG.info("GET /receipts/" + supporterUsername);
        try {
            return new ResponseEntity<>(needReceiptDao.getReceipts(supporterUsername), HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request to retrieve the total of all of the funding a user has done
     * 
     * @param supporterUsername
     * @return ResponseEntity with a Double funding value and HTTP status of OK
     *          <br>
     *          ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{supporterUsername}/total") 
    public ResponseEntity<Double> getUserFundingSum(@PathVariable String supporterUsername) {
        LOG.info("GET /receipts/" + supporterUsername + "/total");
        try {
            return new ResponseEntity<Double>(needReceiptDao.getUserFundingSum(supporterUsername), HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request to retrieve a {@linkplain Map} containing every users total funding amount
     * 
     * @param supporterUsername
     * @return ResponseEntity with a {@linkplain Map} with supporter usernames as keys and funding totals as values
     *          <br>
     *          ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/allUsersFunding") 
    public ResponseEntity<String[]> getSortedUserFunding() {
        LOG.info("GET /receipts/allUsersFunding");
        try {
            return new ResponseEntity<String[]>(needReceiptDao.getSortedUserFunding(), HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
