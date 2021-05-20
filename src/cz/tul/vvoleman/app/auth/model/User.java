package cz.tul.vvoleman.app.auth.model;

import cz.tul.vvoleman.app.address.Address;

import java.time.LocalDateTime;

public class User {

    private final int id;
    private String email;
    private String firstname;
    private String lastname;
    private Address address;
    private final LocalDateTime createdAt;
    private boolean isEnabled;
    private Role role;

    /**
     * User's constructor
     * @param id ID
     * @param email Email
     * @param firstname Firstname
     * @param lastname Lastname
     * @param address Address
     * @param createdAt Created at
     * @param isEnabled is Enabled
     * @param role Role
     */
    public User(int id, String email, String firstname, String lastname, Address address, LocalDateTime createdAt, boolean isEnabled, Role role) {
        this.id = id;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.createdAt = createdAt;
        this.isEnabled = isEnabled;
        this.role = role;
    }

    public int getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public String getFirstname() {
        return firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public Address getAddress() {
        return address;
    }
    public boolean isEnabled() {
        return isEnabled;
    }
    public Role getRole() {
        return role;
    }
}