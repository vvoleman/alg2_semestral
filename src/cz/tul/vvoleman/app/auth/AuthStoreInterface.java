package cz.tul.vvoleman.app.auth;

import cz.tul.vvoleman.app.User;
import cz.tul.vvoleman.utils.exceptions.UnknownUserException;

import java.util.List;

public interface AuthStoreInterface {

    /**
     * Returns user by ID
     * @param id user id
     * @return User
     */
    public User get(int id) throws UnknownUserException;

    /**
     * Returns user by email and password - login
     * @param email email
     * @param password unhashed password
     * @return User
     */
    public User get(String email, String password) throws UnknownUserException;

    /**
     * Returns list of users based on array of IDs
     * @param ids IDs
     * @return List of user
     */
    public List<User> get(int[] ids) throws UnknownUserException;

    /**
     * Returns list of users based on list of IDs
     * @param ids IDs
     * @return List of user
     */
    public List<User> get(List<Integer> ids) throws UnknownUserException;

    /**
     * Saves User to storage
     * @param user User to save
     * @return is user saved?
     */
    public boolean save(User user);

    /**
     * Creates User in storage
     * @param user User
     * @return is user created?
     */
    public boolean create(User user);

}
