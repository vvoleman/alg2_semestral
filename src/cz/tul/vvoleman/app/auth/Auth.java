package cz.tul.vvoleman.app.auth;

import cz.tul.vvoleman.app.User;
import cz.tul.vvoleman.utils.exceptions.auth.AuthException;
import cz.tul.vvoleman.utils.exceptions.auth.EmailExistsException;
import cz.tul.vvoleman.utils.exceptions.storage.StorageException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Auth {

    //Instance of logged user
    private static User loggedIn;

    //Storage
    private static AuthStoreInterface as = new FileAuthStorage();

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

    public static boolean register(UserContainer uc) throws EmailExistsException, AuthException, StorageException {
        //Is there anybody with this email?
        if(as.exists(uc.email)){
            throw new EmailExistsException("User with this email already exists!");
        }

        //Hash the password and save it to rc
        try {
            uc.password = hash(uc.password);
        }catch (SecurityException e){
            throw new AuthException("Unable to hash password");
        }

        as.create(uc);

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


}
