package cz.tul.vvoleman.app.post.storage;

import cz.tul.vvoleman.app.post.PostLibrary;
import cz.tul.vvoleman.app.post.PostOffice;
import cz.tul.vvoleman.app.post.mail.Mail;
import cz.tul.vvoleman.app.post.mail.MailContainer;
import cz.tul.vvoleman.io.Database;
import cz.tul.vvoleman.resource.Datastore;
import cz.tul.vvoleman.utils.exception.auth.AuthException;
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
     * @param psc
     * @return
     */
    @Override
    public PostOffice getByPSC(int psc) {
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

            return PostLibrary.initializeMail(mc);

        } catch (SQLException | PostException e){
            throw new StorageException("Unable to create new mail! "+e.getMessage());
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
}
