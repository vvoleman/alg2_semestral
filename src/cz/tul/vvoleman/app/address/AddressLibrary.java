package cz.tul.vvoleman.app.address;

import java.sql.SQLException;

public class AddressLibrary {

    /**
     * Private constructor
     */
    private AddressLibrary(){}

    /**
     * Returns Address by ID of building
     * @param id ID of building
     * @return null | Address
     * @throws SQLException
     */
    public Address getAddressById(int id) throws SQLException {
        return AddressStore.getInstance().getAddressById(id);
    }

    public Address getAddressByInput(String input){
        //Varianty
        //Plná
        //TODO: Udělej select v DB pro plné adresy (mayhaps)
        return null;
    }

}
