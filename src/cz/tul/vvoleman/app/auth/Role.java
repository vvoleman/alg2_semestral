package cz.tul.vvoleman.app.auth;

import cz.tul.vvoleman.utils.exceptions.auth.RoleException;

public enum Role {

    Customer ("customer"),
    Admin ("admin");

    private String name;

    Role(String name){
        this.name = name;
    }

    String getName(){
        return name;
    }

    public static Role getFromString(String s) throws RoleException {
        s = s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
        try{
            return Role.valueOf(s);
        }catch(IllegalArgumentException e){
            throw new RoleException("Role "+s+" not found");
        }
    }

}
