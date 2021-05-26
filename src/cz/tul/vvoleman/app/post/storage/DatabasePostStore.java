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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabasePostStore implements PostStoreInterface {

    /**
     * Database connection
     */
    private Connection db;

    /**
     * Constructor
     *
     * @throws StorageException Can't access storage
     */
    public DatabasePostStore() throws StorageException {
        try {
            db = Database.getConn(Datastore.getDatas());
        } catch (SQLException e) {
            throw new StorageException("Unable to access storage - " + e.getMessage());
        }
    }

    /**
     * Returns Post Office by PSC
     *
     * @param psc PSC
     * @return PostOffice
     */
    @Override
    public PostOffice getOfficeByPSC(int psc) throws StorageException, PostException {
        String query = "SELECT id,psc,address_id FROM post_offices WHERE psc = ?";
        try {
            PreparedStatement ps = db.prepareStatement(query);
            ps.setInt(1, psc);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                PostOffice po = new PostOffice(
                        rs.getInt(1),
                        rs.getInt(2),
                        AddressLibrary.getAddressById(rs.getInt(3)));
                po.addListToStorage(getMailsWithFilter(-1, psc, null));
                return po;
            }
            throw new PostException("There was no post with this PSC!");
        } catch (SQLException e) {
            throw new StorageException("Unable to access PSC!");
        }
    }

    /**
     * Creates Mail with MailContainer
     *
     * @param mc MailContainer
     * @return Mail
     * @throws StorageException Can't access storage
     */
    @Override
    public Mail create(MailContainer mc) throws StorageException {
        String query = "INSERT INTO mails (sender_id,receiver_address_id,receiver_name,status,type,info,last_changed_at) " +
                "VALUES (?,?,?,?,?,?,?)";

        try {
            mc.lastChangedAt = LocalDateTime.now();
            PreparedStatement ps = db.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, mc.sender.getId());
            ps.setInt(2, mc.receiverAddress.getId());
            ps.setString(3, mc.receiverName);
            ps.setString(4, mc.status.toString());
            ps.setString(5, mc.type);
            ps.setString(6, mc.info);
            ps.setTimestamp(7, Timestamp.valueOf(mc.lastChangedAt));

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                mc.id = rs.getInt(1);
            }

            Mail m = PostLibrary.initializeMail(mc);
            setTextCode(m.getId(), m.getTextId());

            return m;

        } catch (SQLException | PostException e) {
            throw new StorageException("Unable to create new mail! " + e.getMessage());
        }
    }

    /**
     * Changes mail status and location of mail
     * @param m Mail
     * @param officeId ID of PostOffice
     * @throws StorageException Can't access storage
     */
    public void changeMailStatus(Mail m, int officeId) throws StorageException {
        String query = "UPDATE mails SET status = ?";
        ArrayList<Integer> list = new ArrayList<>();

        if (officeId >= 0) {
            query += ", location_id = ?";
            list.add(officeId);
        }

        query += " WHERE id = ?";
        list.add(m.getId());
        try {
            PreparedStatement ps = db.prepareStatement(query);
            ps.setString(1, m.getStatus().toString());
            for (int i = 0; i < list.size(); i++) {
                ps.setInt(i + 2, list.get(i));
            }

            ResultSet rs = ps.executeQuery();
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new StorageException(String.format("Unable to update mail status! (id=?,status=?)", m.getId(), m.getStatus().toString()));
        }
    }

    /**
     * Changes mail status
     * @param m Mail
     * @throws StorageException Can't access storage
     */
    public void changeMailStatus(Mail m) throws StorageException {
        changeMailStatus(m, -1);
    }

    /**
     * Changes statuses and locations of mails
     * @param s Status
     * @param ids List of IDs to change
     * @param officeId PostOffice ID
     * @throws StorageException Can't access storage
     */
    public void changeMailStatus(Status s, List<Integer> ids, int officeId) throws StorageException {
        String query = "UPDATE mails SET status = ?, location_id = ";
        boolean condition = officeId >= 0;
        if (condition) {
            query += "?";
        } else {
            query += "null";
        }
        query += " WHERE id IN " + makeWhereIn(ids);
        try {
            PreparedStatement ps = db.prepareStatement(query);
            ps.setString(1, s.toString());
            if (condition) {
                ps.setInt(2, officeId);
            }

            ResultSet rs = ps.executeQuery();
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new StorageException(String.format("Unable to update mails status! (status=%s)", s.toString()));
        }
    }

    /**
     * Returns mails with filter
     * @param userId ID of user (-1 for not filtering by)
     * @param psc PSC (-1 for not filtering by)
     * @param status Status (null for not filtering by)
     * @return List of mails
     * @throws StorageException Can't access storage
     */
    @Override
    public List<Mail> getMailsWithFilter(int userId, int psc, Status status) throws StorageException {
        String query = getMailQuery();

        boolean bId = userId > 0;
        boolean bPsc = psc > 0;
        boolean bStatus = status != null;

        if (bPsc) {
            query += " JOIN post_offices po ON po.id = location_id";
        }

        ArrayList<Integer> params = new ArrayList<>();
        if (bId || bPsc || bStatus) {
            query += " WHERE";
            if (bId) {
                query += " sender_id = ?";
                params.add(userId);
            }
            if (bPsc) {
                query += ((bId) ? " AND" : "") + " po.psc = ?";
                params.add(psc);
            }
            if (bStatus) {
                query += ((bId) ? " AND" : "") + " status = ?";
            }
        }

        try {
            PreparedStatement ps = db.prepareStatement(query);

            for (int i = 0; i < params.size(); i++) {
                ps.setInt(i + 1, params.get(i));
            }
            if (bStatus) {
                ps.setString(params.size() + 1, status.toString());
            }
            ResultSet rs = ps.executeQuery();
            List<Mail> result = new ArrayList<>();

            while (rs.next()) {
                result.add(getMailFromResult(rs));
            }

            return result;
        } catch (Exception e) {
            throw new StorageException("There was a problem with storage! - " + e.getMessage());
        }
    }

    /**
     * Returns mail by with specified textID
     * @param textId TextID
     * @return Mail
     * @throws StorageException Can't access storage
     * @throws PostException No mail with this textID
     */
    @Override
    public Mail getMailByTextId(String textId) throws StorageException, PostException {
        String query = getMailQuery() + " WHERE text_id = ?";
        try {
            PreparedStatement ps = db.prepareStatement(query);
            ps.setString(1, textId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return getMailFromResult(rs);
            }
        } catch (Exception e) {
            throw new StorageException("Unable to load mail with textId=" + textId);
        }
        throw new PostException("Unknown mail with textId=" + textId);
    }

    /////////////////////////////////////////////////////////

    /**
     * Returns mail from ResultSet
     * @param rs ResultSet
     * @return Mail
     * @throws SQLException Problem with DB
     * @throws UnknownUserException Unknown user
     * @throws StorageException Can't access storage
     * @throws PostException Invalid mail type
     */
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
        mc.officeId = rs.getInt(8);

        return PostLibrary.initializeMail(mc);
    }

    /**
     * Returns base of mail query
     * @return Mail query
     */
    private String getMailQuery() {
        return "SELECT mails.id as id,status,sender_id,receiver_address_id,receiver_name,type,info,location_id FROM mails";
    }

    /**
     * Creates string that simulates WHERE IN
     * @param ids IDs
     * @return String
     */
    private String makeWhereIn(List<Integer> ids) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            s.append(ids.get(i));
            if (i < ids.size() - 1) {
                s.append(",");
            }
        }
        return "(" + s.toString() + ")";
    }

    /**
     * Sets textID for mail with specified ID
     * @param id Mail ID
     * @param textId TextID
     * @throws StorageException Can't access storage
     */
    private void setTextCode(int id, String textId) throws StorageException {
        String query = "UPDATE mails SET text_id = ? WHERE id = ?";

        try {
            PreparedStatement ps = db.prepareStatement(query);
            ps.setString(1, textId);
            ps.setInt(2, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new StorageException("Unable to update textCode!");
        }
    }
}