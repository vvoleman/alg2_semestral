package cz.tul.vvoleman.test.address;

import cz.tul.vvoleman.app.address.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    private Address address;

    @BeforeEach
    @Disabled
    public void setUp(){
        address = new Address(
                17901448,
                "Kollárova",
                226,
                2,
                40003,
                "Ústí nad Labem",
                "Ústí nad Labem"
        );
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Zobrazení celé adresy")
    void getFullAddress() {
        assertEquals("Kollárova 226/2, 400 03 Ústí nad Labem",
                address.getFullAddress());
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Pěkná PSC")
    void getPrettyPsc() {
        assertEquals("400 03",
                address.getPrettyPsc());
    }
}