package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.management.openmbean.KeyAlreadyExistsException;

import com.ufund.api.ufundapi.persistence.NeedDAO;
import com.ufund.api.ufundapi.model.Need;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.ufund.api.ufundapi.model.Need.NeedType;
import com.ufund.api.ufundapi.persistence.NeedDAO;
import com.ufund.api.ufundapi.persistence.UserDAO;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Supporter;
import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.persistence.UserDAO;
import com.ufund.api.ufundapi.persistence.UserFileDAO;

@Tag("Controller-tier")
public class UserFileDAOTests {
    private UserFileDAO userFileDAO;
    private UserDAO userDAO;

    @BeforeEach
    public void setupUserFileDAO() {
        String filename = "ufund-api/data/supporters.json";
        userDAO = mock(UserDAO.class);
        userFileDAO = new UserFileDAO(userDAO, filename);
    }

    @Test
    public void testGetSupporters() throws IOException {
        // setup
        setupUserFileDAO();
        Supporter[] expected = new Supporter[0];
        when(userDAO.getSupporters()).thenReturn(expected);

        // invoke
        Supporter[] actual = userFileDAO.getSupporters();

        // analyze
        assertEquals(expected, actual);
    }

}
