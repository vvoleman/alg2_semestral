package cz.tul.vvoleman.app.auth.storage;

import cz.tul.vvoleman.app.address.AddressLibrary;
import cz.tul.vvoleman.app.auth.Auth;
import cz.tul.vvoleman.app.auth.model.Role;
import cz.tul.vvoleman.app.auth.model.User;
import cz.tul.vvoleman.app.auth.model.UserContainer;
import cz.tul.vvoleman.io.Database;
import cz.tul.vvoleman.resource.Datastore;
import cz.tul.vvoleman.utils.exception.auth.AuthException;
import cz.tul.vvoleman.utils.exception.auth.RoleException;
import cz.tul.vvoleman.utils.exception.auth.UnknownUserException;
import cz.tul.vvoleman.utils.exception.storage.StorageException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class DatabaseAuthStorage implements AuthStoreInterface{

    private Connection db;

    /**
     * Constructor
     * @throws StorageException Can't access storage
     */
    public DatabaseAuthStorage() throws StorageException {
        try{
            db = Database.getConn(Datastore.getDatas());
        } catch (SQLException e){
            throw new StorageException("Unable to access storage - "+e.getMessage());
        }
    }

    ////////////////////////////////////////////////////

    /**
     * Returns user by ID
     *
     * @param id user id
     * @return User
     */
    @Override
    public User get(int id) throws StorageException, UnknownUserException, AuthException {
        String query = getQuery()+" WHERE id = ? LIMIT 1";

        try{
            PreparedStatement ps = db.prepareStatement(query);
            ps.setInt(1,id);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return getUserFromResult(rs);
            }
            throw new UnknownUserException("Unable to find user with ID "+id);
        } catch (SQLException e){
            throw new StorageException("Unable to access storage - "+e.getMessage());
        }

    }

    /**
     * Returns user by email
     *
     * @param email Email
     * @return User
     */
    @Override
    public User get(String email) throws UnknownUserException, StorageException, AuthException {
        String query = getQuery()+" WHERE email = ? LIMIT 1";

        try{
            PreparedStatement ps = db.prepareStatement(query);
            ps.setString(1,email);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return getUserFromResult(rs);
            }
            throw new UnknownUserException("Unable to find user with email "+email);
        } catch (SQLException e){
            throw new StorageException("Unable to access storage - "+e.getMessage());
        }
    }

    /**
     * Returns user by email and password - login
     *
     * @param email    email
     * @param password unhashed password
     * @return User
     */
    @Override
    public User get(String email, String password) throws UnknownUserException, StorageException, AuthException {
        password = Auth.hash(password);
        String query = getQuery()+" WHERE email = ? AND password = ? LIMIT 1";

        try{
            PreparedStatement ps = db.prepareStatement(query);
            ps.setString(1,email);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return getUserFromResult(rs);
            }
            throw new UnknownUserException("Unable to find user with email "+email+" and specified password");
        } catch (SQLException e){
            throw new StorageException("Unable to access storage - "+e.getMessage());
        }
    }

    ////////////////////////////////////////////////////

    /**
     * Returns list of users based on array of IDs
     *
     * @param ids IDs
     * @return List of user
     */
    @Override
    public List<User> get(int[] ids) throws UnknownUserException, StorageException {
        return null;
    }

    /**
     * Returns list of users based on list of IDs
     *
     * @param ids IDs
     * @return List of user
     */
    @Override
    public List<User> get(List<Integer> ids) throws UnknownUserException, StorageException {
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
    public boolean save(User user) throws StorageException {
        //TODO: Změna hesla
        String query = "UPDATE users SET " +
                "email = ?, first_name = ?, last_name = ?, address_id = ?," +
                "created_at = ?, enabled = ?, role = ? WHERE id = ?";

        try{
            PreparedStatement ps = db.prepareStatement(query);
            ps.setString(1,user.getEmail());
            ps.setString(2,user.getEmail());
            ps.setString(3,user.getEmail());
            ps.setInt(4,user.getAddress().getId());
            ps.setTimestamp(5, Timestamp.valueOf(user.getCreatedAt()));
            ps.setBoolean(6,user.isEnabled());
            ps.setString(7,user.getRole().getName());
            ps.setInt(8,user.getId());

            ps.executeUpdate();

            return true;
        } catch(SQLException e){
            throw new StorageException("Unable to save user!");
        }
    }

    /**
     * Creates User in storage
     *
     * @param uc UserContainer
     * @return is user created?
     */
    @Override
    public User create(UserContainer uc) throws StorageException {
        String query = "INSERT INTO users (email,first_name,last_name,address_id,created_at,role) VALUES " +
                "(?,?,?,?,?,?)";

        try{
            uc.createdAt = LocalDateTime.now();

            PreparedStatement ps = db.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,uc.email);
            ps.setString(2,uc.firstName);
            ps.setString(3,uc.lastName);
            ps.setInt(4,uc.address.getId());
            ps.setTimestamp(5,Timestamp.valueOf(uc.createdAt));
            ps.setString(6,uc.role.getName());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();

            if(rs.next()){
                uc.id = rs.getInt(1);
            }
            //TODO: Udělej testy
            return Auth.initializeUser(uc);
        } catch(SQLException | AuthException e){
            throw new StorageException("Unable to create new user!");
        }
    }

    ////////////////////////////////////////////////////

    /**
     * Checks if there is any user with this email
     *
     * @param email Email
     * @return true if exists
     */
    @Override
    public boolean exists(String email) {
        return false;
    }

    /**
     * Checks if there is any user with this id
     *
     * @param id ID
     * @return true if exists
     */
    @Override
    public boolean exists(int id) {
        return false;
    }

    ////////////////////////////////////////////////////

    private String getQuery(){
        return "SELECT id,email,password,first_name,last_name,address_id,created_at,enabled,role FROM users";
    }

    private User getUserFromResult(ResultSet rs) throws SQLException, AuthException {
        return new User(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                AddressLibrary.getAddressById(rs.getInt(5)),
                rs.getTimestamp(6).toLocalDateTime(),
                rs.getBoolean(7),
                Role.getFromString(rs.getString(8))
        );
    }
}
