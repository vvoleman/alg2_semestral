package cz.tul.vvoleman.test.auth;

import cz.tul.vvoleman.app.address.AddressLibrary;
import cz.tul.vvoleman.app.auth.Auth;
import cz.tul.vvoleman.app.auth.model.Role;
import cz.tul.vvoleman.app.auth.model.UserContainer;
import cz.tul.vvoleman.resource.Datastore;
import cz.tul.vvoleman.utils.exception.address.BadAddressFormatException;
import cz.tul.vvoleman.utils.exception.auth.AuthException;
import cz.tul.vvoleman.utils.exception.auth.EmailExistsException;
import cz.tul.vvoleman.utils.exception.storage.StorageException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AuthTest {

    @Test
    @DisplayName("Hashování")
    void hash() {
        String password = "mojetajneheslo";

        assertEquals(
                "9255d313f3f60aadae470eb548d04dec0b92a7efe2ddf00b0a4b5e92a95ed0beafff09d96cca61abe43df04cbab27642005b62482af400963c1b0595b8276a48",
                Auth.hash(password),
                "Hash"
        );

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
    }

    @Test
    @DisplayName("Přihlášení")
    void login() throws StorageException {
        System.out.println("storageUsed: "+ Datastore.getStorageUsed());

        String email = "marco@polo.cz";
        String password = "mojeheslo";

        assertTrue(Auth.login(email,password),"Kontrola přihlášení");
    }
}