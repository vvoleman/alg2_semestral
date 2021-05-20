package cz.tul.vvoleman.app.auth;

import cz.tul.vvoleman.app.User;
import cz.tul.vvoleman.utils.exceptions.auth.RoleException;
import cz.tul.vvoleman.utils.exceptions.storage.StorageException;
import cz.tul.vvoleman.utils.exceptions.auth.UnknownUserException;

import java.util.List;

public interface AuthStoreInterface {

    /**
     * Returns user by ID
     * @param id user id
     * @return User
     */
    public User get(int id) throws UnknownUserException, StorageException, RoleException;

    /**
     * Returns user by email
     * @param email Email
     * @return User
     */
    public User get(String email) throws UnknownUserException, StorageException, RoleException;

    /**
     * Returns user by email and password - login
     * @param email email
     * @param password unhashed password
     * @return User
     */
    public User get(String email, String password) throws UnknownUserException, StorageException;

    ////////////////////////////////////////////////////

    /**
     * Returns list of users based on array of IDs
     * @param ids IDs
     * @return List of user
     */
    public List<User> get(int[] ids) throws UnknownUserException, StorageException;

    /**
     * Returns list of users based on list of IDs
     * @param ids IDs
     * @return List of user
     */
    public List<User> get(List<Integer> ids) throws UnknownUserException, StorageException;

    ////////////////////////////////////////////////////

    /**
     * Saves User to storage
     * @param user User to save
     * @return is user saved?
     */
    public boolean save(User user);

    /**
     * Creates User in storage
     * @param uc UserContainer
     * @return is user created?
     */
    public boolean create(UserContainer uc) throws StorageException;

    ////////////////////////////////////////////////////

    /**
     * Checks if there is any user with this email
     * @param email Email
     * @return true if exists
     */
    public boolean exists(String email);

    /**
     * Checks if there is any user with this id
     * @param id ID
     * @return true if exists
     */
    public boolean exists(int id);

}