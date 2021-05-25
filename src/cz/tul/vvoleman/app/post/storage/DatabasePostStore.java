package cz.tul.vvoleman.app.post.storage;

import cz.tul.vvoleman.app.address.AddressLibrary;
import cz.tul.vvoleman.app.auth.Auth;
import cz.tul.vvoleman.app.post.PostLibrary;
import cz.tul.vvoleman.app.post.PostOffice;
import cz.tul.vvoleman.app.post.mail.Mail;
import cz.tul.vvoleman.app.post.mail.MailContainer;
import cz.tul.vvoleman.app.post.mail.Status;
import cz.tul.vvoleman.io.Database;
import cz.tul.vvoleman.resource.Datastore;
import cz.tul.vvoleman.utils.exception.auth.UnknownUserException;
import cz.tul.vvoleman.utils.exception.post.PostException;
import cz.tul.vvoleman.utils.exception.storage.StorageException;

import java.sql.*;

public class DatabasePostStore implements PostStoreInterface{

    private Connection db;

    /**
     * Constructor
     * @throws StorageException Can't access storage
     */
    public DatabasePostStore() throws StorageException {
        try{
            db = Database.getConn(Datastore.getDatas());
        } catch (SQLException e){
            throw new StorageException("Unable to access storage - "+e.getMessage());
        }
    }

    /**
     * Returns Post Office by PSC
     *
     * @param psc PSC
     * @return PostOffice
     */
    @Override
    public PostOffice getOfficeByPSC(int psc) {
        return null;
    }

    @Override
    public Mail create(MailContainer mc) throws StorageException {
        String query = "INSERT INTO mails (sender_id,receiver_address_id,receiver_name,status,type,info) " +
                "VALUES (?,?,?,?,?,?)";

        try{
            PreparedStatement ps = db.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1,mc.sender.getId());
            ps.setInt(2,mc.receiverAddress.getId());
            ps.setString(3, mc.receiverName);
            ps.setString(4,mc.status.toString());
            ps.setString(5,mc.type);
            ps.setString(6,mc.info);
            System.out.println(ps.toString());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                mc.id = rs.getInt(1);
            }

            Mail m = PostLibrary.initializeMail(mc);
            setTextCode(m.getId(),m.getTextId());

            return m;

        } catch (SQLException | PostException e){
            throw new StorageException("Unable to create new mail! "+e.getMessage());
        }
    }

    private void setTextCode(int id, String textId) throws StorageException {
        String query = "UPDATE mails SET text_id = ? WHERE id = ?";

        try{
            PreparedStatement ps = db.prepareStatement(query);
            ps.setString(1,textId);
            ps.setInt(2,id);

            ps.executeUpdate();
        }catch (SQLException e){
            throw new StorageException("Unable to update textCode!");
        }
    }

    public void changeMailStatus(Mail m,int officeId) throws StorageException {
        String query = "UPDATE mails SET status = ?";
        if(officeId >= 0){
            query+=", location_id = ?";
        }
        query += "WHERE id = ?";
        try{
            PreparedStatement ps = db.prepareStatement(query);
            ps.setString(1,m.getStatus().toString());
            ps.setInt(2,m.getId());

            ResultSet rs = ps.executeQuery();
            ps.executeUpdate();
        }catch (SQLException e){
            throw new StorageException(String.format("Unable to update mail status! (id=?,status=?)",m.getId(),m.getStatus().toString()));
        }
    }

    public void changeMailStatus(Mail m) throws StorageException {
        changeMailStatus(m,-1);
    }

    @Override
    public Mail getMailByTextId(String textId) throws StorageException, PostException {
        String query = getMailQuery()+" WHERE text_id = ?";
        try{
            PreparedStatement ps = db.prepareStatement(query);
            ps.setString(1,textId);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return getMailFromResult(rs);
            }
        }catch (Exception e){
            throw new StorageException("Unable to load mail with textId="+textId);
        }
        throw new PostException("Unknown mail with textId="+textId);
    }

    private Mail getMailFromResult(ResultSet rs) throws SQLException, UnknownUserException, StorageException, PostException {
        MailContainer mc = new MailContainer(
                rs.getInt(1),
                Status.valueOf(rs.getString(2)),
                Auth.getUser(rs.getInt(3)),
                AddressLibrary.getAddressById(rs.getInt(4)),
                rs.getString(5),
                rs.getString(6),
                rs.getString(7)
        );

        return PostLibrary.initializeMail(mc);
    }

    private String getMailQuery(){
        return "SELECT id,status,sender_id,receiver_address_id,receiver_name,type,info,location_id FROM mails";
    }
}
