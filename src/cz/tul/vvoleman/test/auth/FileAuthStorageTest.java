package cz.tul.vvoleman.test.auth;

import cz.tul.vvoleman.app.address.AddressLibrary;
import cz.tul.vvoleman.app.auth.Auth;
import cz.tul.vvoleman.app.auth.storage.FileAuthStorage;
import cz.tul.vvoleman.app.auth.model.Role;
import cz.tul.vvoleman.app.auth.model.UserContainer;
import cz.tul.vvoleman.resource.Datastore;
import cz.tul.vvoleman.utils.exception.address.BadAddressFormatException;
import cz.tul.vvoleman.utils.exception.auth.AuthException;
import cz.tul.vvoleman.utils.exception.auth.EmailExistsException;
import cz.tul.vvoleman.utils.exception.auth.RoleException;
import cz.tul.vvoleman.utils.exception.storage.StorageException;
import cz.tul.vvoleman.utils.exception.auth.UnknownUserException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileAuthStorageTest {

    private FileAuthStorage fas = new FileAuthStorage();

    @Test
    @DisplayName("getUserById")
    @Disabled
    void getById() throws UnknownUserException, StorageException, AuthException {
        assertEquals("vojtavol@email.cz",fas.get(1).getEmail(),"Uživatel s ID 1");
        assertEquals("vvoleman@email.cz",fas.get(2).getEmail(),"Uživatel s ID 2");
    }

    @Test
    @DisplayName("Existuje email?")
    @Disabled
    void getByEmail() throws RoleException, UnknownUserException, StorageException, AuthException {
        assertEquals(1,fas.get("vojtavol@email.cz").getId(),"Uživatel s emailem vojtavol@email.cz");
    }

    @Test
    @DisplayName("Container to line")
    void containerToLine() throws SQLException, BadAddressFormatException, AuthException {
        String correct = "1,vojtavol@email.cz,fds,Vojtěch,Voleman,17901448,"+LocalDateTime.now().format(Datastore.getDtf())+",1,admin";

        UserContainer uc = getUc();
        assertEquals(correct,fas.containerToLine(uc),"ContainerToLine");
    }

    @Test
    @DisplayName("Vytvoření nového uživatele")
    void create() throws StorageException, SQLException, BadAddressFormatException, EmailExistsException, AuthException {
        UserContainer uc = new UserContainer();
        uc.firstName = "Marco";
        uc.lastName = "Polo";
        uc.email = "marco@polo.cz";
        uc.password = "mojeheslo";
        uc.address = AddressLibrary.getAddressByInput("Kollárova 226/2, 40003");
        uc.role = Role.Customer;

        assertTrue(Auth.register(uc));
        System.out.println("Je uživatel připojen? "+Auth.isLoggedIn());
        Auth.logout();
        System.out.println("Je uživatel připojen? "+Auth.isLoggedIn());
    }

    private UserContainer getUc() throws SQLException, BadAddressFormatException {
        UserContainer uc = new UserContainer();
        uc.firstName = "Vojtěch";
        uc.lastName = "Voleman";
        uc.email = "vojtavol@email.cz";
        uc.password = "fds";
        uc.address = AddressLibrary.getAddressByInput("Kollárova 226/2, 40003");
        uc.createdAt = LocalDateTime.now();
        uc.role = Role.Admin;
        uc.id = 1;
        return uc;
    }
}