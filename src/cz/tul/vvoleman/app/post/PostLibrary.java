package cz.tul.vvoleman.app.post;

import cz.tul.vvoleman.app.auth.Auth;
import cz.tul.vvoleman.app.auth.storage.AuthStoreInterface;
import cz.tul.vvoleman.app.post.mail.*;
import cz.tul.vvoleman.app.post.mail.Package;
import cz.tul.vvoleman.app.post.storage.PostStoreInterface;
import cz.tul.vvoleman.resource.Datastore;
import cz.tul.vvoleman.utils.exception.post.PostException;
import cz.tul.vvoleman.utils.exception.storage.StorageException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostLibrary {

    //Selected WorkStation
    private static PostalInterface workstation;

    /**
     * Instance of an postStore
     */
    private PostStoreInterface ps;

    /**
     * Instance of an PostLibrary (for singleton)
     */
    private static PostLibrary instance;

    /**
     * Cache for offices
     */
    private static Map<Integer, PostOffice> offices = new HashMap<>();

    /**
     * Central warehouse
     */
    private static Warehouse centrum = new Warehouse();

    ////////////////////////////////////////////////////////////////////////////

    /**
     * Returns postStore
     *
     * @return PostStore
     */
    private PostStoreInterface getPostStore() {
        return ps;
    }

    /**
     * Constructor of PostLibrary
     *
     * @throws StorageException Can't access storage
     */
    private PostLibrary() throws StorageException {
        ps = Datastore.getStorageContainer().postStore;
    }

    /**
     * Returns postStore
     *
     * @return PostStore
     * @throws StorageException Can't access storage
     */
    private static PostStoreInterface getStorage() throws StorageException {
        if (instance == null) {
            instance = new PostLibrary();
        }
        return instance.getPostStore();
    }

    ////////////////////////////////////////////////////////////////////////////

    /**
     * Is workstation set?
     *
     * @return true if yes
     */
    public static boolean hasWorkstation() {
        return workstation != null;
    }

    /**
     * Sets workstation to PostalInterface
     *
     * @param pi PostalInterface
     */
    public static void setWorkstation(PostalInterface pi) {
        workstation = pi;
    }

    /**
     * Returns workstation
     *
     * @return Workstation
     */
    public static PostalInterface getWorkstation() {
        return workstation;
    }

    ////////////////////////////////////////////////////////////////////////////

    /**
     * Returns mail with specified textID
     *
     * @param id TextID
     * @return Mail
     * @throws PostException    Mail not found
     * @throws StorageException Can't access storage
     */
    public static Mail getMailByTextId(String id) throws PostException, StorageException {
        return getStorage().getMailByTextId(id);
    }

    /**
     * Returns office by PSC
     *
     * @param psc PSC
     * @return PostOffice
     * @throws StorageException Can't access storage
     * @throws PostException    PostOffice not found
     */
    public static PostOffice getOfficeByPSC(int psc) throws StorageException, PostException {
        if (!offices.containsKey(psc)) {
            offices.put(psc, getStorage().getOfficeByPSC(psc));
        }
        return offices.get(psc);
    }

    /**
     * Returns central warehouse
     *
     * @return Warehouse
     */
    public static Warehouse getCenterWarehouse() {
        return centrum;
    }

    /**
     * Creates mail via MailContainer
     *
     * @param mc MailContainer
     * @return Mail
     * @throws StorageException Can't access storage
     */
    public static Mail create(MailContainer mc) throws StorageException {
        return getStorage().create(mc);
    }

    /**
     * Changes mail status
     *
     * @param m Mail
     * @throws StorageException Can't access storage
     */
    public static void changeMailStatus(Mail m) throws StorageException {
        getStorage().changeMailStatus(m);
    }

    /**
     * Changes status and locationID of mails with IDs in ids
     *
     * @param s          Status
     * @param ids        IDs
     * @param locationId LocationID
     * @throws StorageException Can't access storage
     */
    public static void changeMailStatus(Status s, List<Integer> ids, int locationId) throws StorageException {
        getStorage().changeMailStatus(s, ids, locationId);
    }

    /**
     * Changes mail status and locationID
     *
     * @param m        Mail
     * @param officeId LocationID
     * @throws StorageException Can't access storage
     */
    public static void changeMailStatus(Mail m, int officeId) throws StorageException {
        getStorage().changeMailStatus(m, officeId);
    }

    /**
     * Makes instance of Mail from MailContainer
     *
     * @param mc MailContainer
     * @return Mail
     * @throws PostException There was a problem with initialization
     */
    public static Mail initializeMail(MailContainer mc) throws PostException {
        if (!mc.isReady()) throw new IllegalArgumentException("MailContainer is not ready!");

        switch (mc.type) {
            case "letter":
                return new Letter(
                        mc.id, mc.status, mc.sender, mc.receiverAddress, mc.receiverName, Letter.Type.valueOf(mc.info),
                        mc.officeId, mc.lastChangedAt
                );
            case "package":
                return new Package(
                        mc.id, mc.status, mc.sender, mc.receiverAddress, mc.receiverName, Package.Type.valueOf(mc.info),
                        mc.officeId, mc.lastChangedAt
                );
        }
        throw new PostException("Invalid mail type!");
    }

    /**
     * Filters mails with userID and psc
     *
     * @param userId UserID
     * @param psc    PSC
     * @return Filtered mails
     * @throws StorageException Can't access storage
     */
    public static List<Mail> filterMails(int userId, int psc) throws StorageException {
        return getStorage().getMailsWithFilter(userId, psc, null);
    }

    /**
     * Filters mails with userID, psc and status
     *
     * @param userId UserID
     * @param psc    PSC
     * @param s      Status
     * @return Filtered mails
     * @throws StorageException Can't access storage
     */
    public static List<Mail> filterMails(int userId, int psc, Status s) throws StorageException {
        return getStorage().getMailsWithFilter(userId, psc, s);
    }

    /**
     * Filters mails with status
     *
     * @param s Status
     * @return Filtered mails
     * @throws StorageException Can't access storage
     */
    public static List<Mail> filterMails(Status s) throws StorageException {
        return getStorage().getMailsWithFilter(-1, -1, s);
    }

    /**
     * Returns textID
     *
     * @param id   ID of mail
     * @param type Type of mail (Package/Letter)
     * @param info Subtype of mail
     * @return String
     */
    public static String makeTextId(int id, String type, String info) {
        String textId;
        switch (type) {
            case "letter":
                type = Letter.suffix;
                info = Letter.Type.valueOf(info).getSuffix();
                break;
            case "package":
                type = Package.suffix;
                break;
            default:
                return null;
        }

        return String.format("CZ%s%s%010d", info, type, id);
    }

}
