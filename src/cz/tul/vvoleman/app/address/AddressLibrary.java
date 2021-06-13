package cz.tul.vvoleman.app.address;

import cz.tul.vvoleman.utils.exception.address.BadAddressFormatException;

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
     * @throws SQLException something goes wrong
     */
    public static Address getAddressById(int id) throws SQLException {
        return AddressStore.getInstance().getAddressById(id);
    }

    /**
     * Returns Address based on input<br>
     * <b>Allowed formats: </b>
     * <li>[streetName/municipalityName] [houseNumber?/additionalNumber] ?, [psc]</li>
     * @param input text address
     * @return Address|Null
     * @throws BadAddressFormatException address is incorrectly formed
     * @throws SQLException when something goes wrong
     */
    public static Address getAddressByInput(String input) throws BadAddressFormatException, SQLException {
        //Street name, numbers, psc
        String[] parts = splitString(input);

        if(parts.length < 3 || parts[2].length() != 5){
            throw new BadAddressFormatException("Incorrect format of address");
        }

        //Zkontroluj čísla - jestli jsou ve formátu xx/xx nebo jenom xx, když jinak tak vyhoď vyjímku
        String[] number_parts = parts[1].split("/");
        int[] numbers = new int[2];
        try {
            for (int i = 0; i < number_parts.length; i++) {
                numbers[i] = Integer.parseInt(number_parts[i]);
            }

            //Vrátím adresu
            return AddressStore.getInstance().getAddressByValues(
                    parts[0],
                    numbers[0],
                    numbers[1],
                    Integer.parseInt(parts[2])
            );
        }catch (SQLException e){
            throw e;
        }catch (Exception e){
            throw new BadAddressFormatException("Incorrect format of address - "+input);
        }
    }

    /**
     * Separates string to parts for address
     * @param s input
     * @return parts
     */
    private static String[] splitString(String s){
        return s.replace(",","").split(" ");
    }

}
