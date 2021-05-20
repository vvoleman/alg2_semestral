package cz.tul.vvoleman.app.auth.model;

import cz.tul.vvoleman.utils.exception.auth.AuthException;
import cz.tul.vvoleman.utils.exception.auth.RoleException;

public enum Role {

    //Enums
    Customer ("customer"),
    Admin ("admin");

    //Property
    private String name;

    //Constructor
    Role(String name){
        this.name = name;
    }

    /**
     * Returns String name of enum
     * @return name
     */
    public String getName(){
        return name;
    }

    /**
     * Returns enum from string
     * @param s string
     * @return Role
     * @throws RoleException role doesn't exists
     */
    public static Role getFromString(String s) throws AuthException {
        s = s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
        try{
            return Role.valueOf(s);
        }catch(IllegalArgumentException e){
            throw new AuthException("Role "+s+" not found");
        }
    }

}
