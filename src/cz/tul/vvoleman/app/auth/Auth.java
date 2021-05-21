package cz.tul.vvoleman.app.auth;

import cz.tul.vvoleman.app.auth.model.User;
import cz.tul.vvoleman.app.auth.model.UserContainer;
import cz.tul.vvoleman.app.auth.storage.AuthStoreInterface;
import cz.tul.vvoleman.app.auth.storage.DatabaseAuthStorage;
import cz.tul.vvoleman.app.auth.storage.FileAuthStorage;
import cz.tul.vvoleman.resource.Datastore;
import cz.tul.vvoleman.utils.exception.auth.AuthException;
import cz.tul.vvoleman.utils.exception.auth.EmailExistsException;
import cz.tul.vvoleman.utils.exception.auth.RoleException;
import cz.tul.vvoleman.utils.exception.auth.UnknownUserException;
import cz.tul.vvoleman.utils.exception.storage.StorageException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Auth {

    //Instance of logged user
    private static User loggedIn;

    //Storage
    private AuthStoreInterface as;

    private static Auth instance;

    private AuthStoreInterface getAuthStorage(){
        return as;
    }

    private Auth() throws StorageException {
        as = Datastore.getStorageContainer().authStore;
    }

    private static AuthStoreInterface getStorage() throws StorageException {
        if(instance == null){
            instance = new Auth();
        }
        return instance.getAuthStorage();
    }

    ///////////////////////////////////////////////

    /**
     * Is somebody logged in?
     * @return true if logged in
     */
    public static boolean isLoggedIn(){
        return loggedIn != null;
    }

    /**
     * Returns logged user
     * @return User|Null if not logged
     */
    public static User getUser(){
        if(isLoggedIn()){
            return loggedIn;
        }
        return null;
    }

    /**
     * Logs out a user
     */
    public static void logout(){
        loggedIn = null;
    }

    public static boolean login(String email, String password) throws StorageException {
        try{
            loggedIn = getStorage().get(email,password);
            return true;
        } catch (UnknownUserException | AuthException e){
            return false;
        }
    }

    /**
     * Registers new user
     * @param uc User container
     * @return true if successfuly registered
     * @throws EmailExistsException Email is already taken
     * @throws AuthException Unable to create new user
     * @throws StorageException Problem with storage
     */
    public static boolean register(UserContainer uc) throws EmailExistsException, AuthException, StorageException {
        //Is there anybody with this email?
        if(getStorage().exists(uc.email)){
            throw new EmailExistsException("User with this email already exists!");
        }

        //Hash the password and save it to rc
        try {
            uc.password = hash(uc.password);
        }catch (SecurityException e){
            throw new AuthException("Unable to hash password");
        }

        User u = getStorage().create(uc);
        if(u != null){
            loggedIn = u;
            return true;
        }
        return false;
    }

    /**
     * Returns hashed password
     * @param password Password to hash
     * @return Hashed password
     */
    public static String hash(String password){
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException("Unable to create hash");
        }

        md.update("42".getBytes(StandardCharsets.UTF_8));

        byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    /**
     * Returns initialized user created from UserContainer
     * @param uc Data
     * @return User
     * @throws AuthException Unable to create user
     */
    public static User initializeUser(UserContainer uc) throws AuthException {
        if(!uc.isReady()) throw new AuthException("Unable to create user");

        return new User(
                uc.id,
                uc.email,
                uc.firstName,
                uc.lastName,
                uc.address,
                uc.createdAt,
                uc.enabled,
                uc.role
        );
    }


}
