package cz.tul.vvoleman.app.auth.model;

import cz.tul.vvoleman.app.address.Address;

import java.time.LocalDateTime;

public class UserContainer {

    public Role role;
    public String email, password, firstName, lastName;
    public int id = -1;
    public Address address;
    public LocalDateTime createdAt;
    public boolean enabled = true;

    public boolean isReady(){
        return role != null && email != null && password != null && firstName != null && lastName != null && id != -1 && address != null && createdAt != null;
    }

}
