package cz.tul.vvoleman.test.address;

import cz.tul.vvoleman.app.address.Address;
import cz.tul.vvoleman.app.address.AddressLibrary;
import cz.tul.vvoleman.utils.exception.address.BadAddressFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AddressLibraryTest {

    @Test
    @DisplayName("Získání adresy ze vstupu")
    void getAddressByInput() throws BadAddressFormatException, SQLException {
        //Textové adresy
        String[] addresses = {
                "Kollárova 226/2, 40003",
                "Krupá 114, 27009 Rakovník"
        };

        //id k adresám
        int[] ids = {17901448,8553203};
        Address a;

        for (int i = 0; i < ids.length; i++) {
            a = AddressLibrary.getAddressByInput(addresses[i]);
            assertNotNull(a,"Adresa "+addresses[i]+" je neplatná");
            assertEquals(ids[i],a.getId(),"Porovnávám ID: "+ids[i]);
        }
    }

    @Test
    @DisplayName("Existuje adresa?")
    void addressExists() throws SQLException, BadAddressFormatException {
        String s = "Studentská 1402/2 46001";
        Address a = AddressLibrary.getAddressByInput(s);
        assertNotNull(a,"Adresa neexistuje!");
        System.out.println(a.getFullAddress());
    }

}