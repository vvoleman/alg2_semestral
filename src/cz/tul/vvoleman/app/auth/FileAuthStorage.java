package cz.tul.vvoleman.app.auth;

import cz.tul.vvoleman.app.User;
import cz.tul.vvoleman.app.address.AddressLibrary;
import cz.tul.vvoleman.resources.Datastore;
import cz.tul.vvoleman.ui.io.TextFileReader;
import cz.tul.vvoleman.utils.exceptions.auth.AuthException;
import cz.tul.vvoleman.utils.exceptions.auth.RoleException;
import cz.tul.vvoleman.utils.exceptions.storage.StorageException;
import cz.tul.vvoleman.utils.exceptions.auth.UnknownUserException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

public class FileAuthStorage implements AuthStoreInterface{

    /**
     * File where all auth data are saved
     */
    private final File f = new File(Datastore.getAuthStorageFile());

    ////////////////////////////////////////////////////

    /**
     * Returns user by ID
     *
     * @param id user id
     * @return User
     */
    @Override
    public User get(int id) throws UnknownUserException, StorageException, RoleException {
        //Získáme uživetele dle filteru
        List<String[]> data = extendedGet((p) -> p[0].equalsIgnoreCase(Integer.toString(id)),1);

        //Pokud nám to nenašlo data
        if(data.size() == 0){
            throw new UnknownUserException("Unable to find user with ID "+id);
        }

        //Vrátíme instanci uživatele
        return initializeUser(data.get(0));

    }

    /**
     * Returns user by email
     *
     * @param email Email
     * @return User
     */
    @Override
    public User get(String email) throws UnknownUserException, StorageException, RoleException {
        //Získáme uživetele dle filteru
        List<String[]> data = extendedGet((p) -> p[1].equalsIgnoreCase(email),1);

        //Pokud nám to nenašlo data
        if(data.size() == 0){
            throw new UnknownUserException("Unable to find user with email "+email);
        }

        //Vrátíme instanci uživatele
        return initializeUser(data.get(0));
    }

    /**
     * Returns user by email and password - login
     *
     * @param email    email
     * @param password unhashed password
     * @return User
     */
    @Override
    public User get(String email, String password) throws UnknownUserException {
        return null;
    }

    ////////////////////////////////////////////////////

    /**
     * Returns list of users based on array of IDs
     *
     * @param ids IDs
     * @return List of user
     */
    @Override
    public List<User> get(int[] ids) throws UnknownUserException {
        return null;
    }

    /**
     * Returns list of users based on list of IDs
     *
     * @param ids IDs
     * @return List of user
     */
    @Override
    public List<User> get(List<Integer> ids) throws UnknownUserException {
        return null;
    }

    ////////////////////////////////////////////////////

    /**
     * Saves User to storage
     *
     * @param user User to save
     * @return is user saved?
     */
    @Override
    public boolean save(User user) {
        return false;
    }

    /**
     * Creates User in storage
     *
     * @param uc UserContainer
     * @return is user created?
     */
    @Override
    public boolean create(UserContainer uc) throws StorageException {
        int newId = getLastID()+1;
        LocalDateTime ldt = LocalDateTime.now();

        uc.id = newId;
        uc.createdAt = ldt;


        return false;
    }

    ////////////////////////////////////////////////////

    /**
     * Checks if there is any user with this email
     * @param email Email
     * @return true if exists
     */
    @Override
    public boolean exists(String email){
        try {
            List<String[]> data = extendedGet((p) -> p[1].equalsIgnoreCase(email), 1);

            return data.size() > 0;
        } catch (StorageException e){
            return false;
        }
    }

    /**
     * Checks if there is any user with this id
     * @param id ID
     * @return true if exists
     */
    @Override
    public boolean exists(int id) {
        try {
            List<String[]> data = extendedGet((p) -> p[0].equalsIgnoreCase(Integer.toString(id)), 1);

            return data.size() > 0;
        } catch (StorageException e){
            return false;
        }
    }

    ////////////////////////////////////////////////////
    public String containerToLine(UserContainer uc){
        if(!uc.isReady()) return null;

        return String.format("%d,%s,%s,%s,%s,%d,%s,%d,%s",
                    uc.id,uc.email,uc.password,uc.firstName,uc.lastName,
                    uc.address.getId(),uc.createdAt.format(Datastore.getDtf()),
                    uc.enabled ? 1 : 0,uc.role.getName()
                );
    }

    /**
     * Returns initialized user created from UserContainer
     * @param uc Data
     * @return User
     * @throws AuthException Unable to create user
     */
    private User initializeUser(UserContainer uc) throws AuthException {
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

    /**
     * Returns initialized user created from parts
     * @param parts Data
     * @return User
     * @throws StorageException Bad format of storage
     * @throws RoleException Invalid role
     */
    private User initializeUser(String[] parts) throws StorageException, RoleException {
        if(parts.length != Datastore.getAuthColumnsSize()){
            throw new StorageException("Bad format of storage!");
        }
        try{
            return new User(
                    //id
                    Integer.parseInt(parts[0]),
                    //email
                    parts[1],
                    //firstname
                    parts[3],
                    //lastname
                    parts[4],
                    //address
                    AddressLibrary.getAddressById(Integer.parseInt(parts[5])),
                    //created_at
                    LocalDateTime.parse(parts[6],Datastore.getDtf()),
                    //enabled
                    parts[7].equalsIgnoreCase("1"),
                    //role
                    Role.getFromString(parts[8])
            );
        }catch (Exception e){
            throw new RoleException("Invalid role");
        }
    }

    private List<String[]> extendedGet(Predicate<String[]> filter, int limit) throws StorageException {
        List<String[]> data;

        //Načteme uživatelova data ze souboru
        try {
            data = TextFileReader.readWithFilter(f,Datastore.getDelimiter(),true,filter,limit);
        } catch (IOException e) {
            throw new StorageException("Unable to access storage!");
        }

        return data;
    }

    private int getLastID() throws StorageException {
        try{
            return Integer.parseInt(TextFileReader.readLastLine(f,Datastore.getDelimiter())[0]);
        }catch (IOException e){
            throw new StorageException("Unable to read storage!");
        }catch (Exception e){
            return 0;
        }
    }
}
