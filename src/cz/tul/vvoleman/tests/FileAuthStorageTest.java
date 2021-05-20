package cz.tul.vvoleman.tests;

import cz.tul.vvoleman.app.address.AddressLibrary;
import cz.tul.vvoleman.app.auth.FileAuthStorage;
import cz.tul.vvoleman.app.auth.Role;
import cz.tul.vvoleman.app.auth.UserContainer;
import cz.tul.vvoleman.resources.Datastore;
import cz.tul.vvoleman.utils.exceptions.address.BadAddressFormatException;
import cz.tul.vvoleman.utils.exceptions.auth.RoleException;
import cz.tul.vvoleman.utils.exceptions.storage.StorageException;
import cz.tul.vvoleman.utils.exceptions.auth.UnknownUserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileAuthStorageTest {

    private FileAuthStorage fas = new FileAuthStorage();

    @Test
    @DisplayName("getUserById")
    void getById() throws RoleException, UnknownUserException, StorageException {
        assertEquals("vojtavol@email.cz",fas.get(1).getEmail(),"Uživatel s ID 1");
        assertEquals("vvoleman@email.cz",fas.get(2).getEmail(),"Uživatel s ID 2");
    }

    @Test
    @DisplayName("getUserByEmail")
    void getByEmail() throws RoleException, UnknownUserException, StorageException {
        assertEquals(1,fas.get("vojtavol@email.cz").getId(),"Uživatel s emailem vojtavol@email.cz");
        assertEquals(2,fas.get("vvoleman@email.cz").getId(),"Uživatel s emailem vvoleman@email.cz");
    }

    @Test
    @DisplayName("Container to line")
    void containerToLine() throws SQLException, BadAddressFormatException {
        String correct = "1,vojtavol@email.cz,fds,Vojtěch,Voleman,17901448,"+LocalDateTime.now().format(Datastore.getDtf())+",1,admin";

        UserContainer uc = new UserContainer();
        uc.firstName = "Vojtěch";
        uc.lastName = "Voleman";
        uc.email = "vojtavol@email.cz";
        uc.password = "fds";
        uc.address = AddressLibrary.getAddressByInput("Kollárova 226/2, 40003");
        uc.createdAt = LocalDateTime.now();
        uc.role = Role.Admin;
        uc.id = 1;

        assertEquals(correct,fas.containerToLine(uc),"ContainerToLine");
    }
}