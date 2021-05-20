package cz.tul.vvoleman.tests;

import cz.tul.vvoleman.app.address.AddressLibrary;
import cz.tul.vvoleman.app.auth.Role;
import cz.tul.vvoleman.app.auth.UserContainer;
import cz.tul.vvoleman.utils.exceptions.address.BadAddressFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserContainerTest {

    @Test
    @DisplayName("isReady")
    void isReady() throws SQLException, BadAddressFormatException {
        UserContainer uc = new UserContainer();
        uc.firstName = "Vojtěch";
        uc.lastName = "Voleman";
        assertFalse(uc.isReady(), "Test #1");
        uc.email = "vojtavol@email.cz";
        uc.password = "ff";
        uc.address = AddressLibrary.getAddressByInput("Kollárova 226/2, 40003");
        uc.createdAt = LocalDateTime.now();
        uc.role = Role.Admin;
        assertFalse(uc.isReady(),"Test #2");
        uc.id = 1;
        assertTrue(uc.isReady(),"Test #3");
    }
}