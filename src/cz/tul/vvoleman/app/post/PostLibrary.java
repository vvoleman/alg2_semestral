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

    private PostStoreInterface ps;

    private static PostLibrary instance;

    private PostStoreInterface getPostStore(){
        return ps;
    }

    private PostLibrary() throws StorageException {
        ps = Datastore.getStorageContainer().postStore;
    }

    private static PostStoreInterface getStorage() throws StorageException {
        if(instance == null){
            instance = new PostLibrary();
        }
        return instance.getPostStore();
    }

    ////////////////////////////////////////////////////////////////////////////

    public static boolean hasWorkstation(){
        return workstation != null;
    }

    public static void setWorkstation(PostalInterface pi){
        workstation = pi;
    }

    public static PostalInterface getWorkstation(){
        return workstation;
    }

    ////////////////////////////////////////////////////////////////////////////

    //PSC,PostOffice
    private static Map<Integer,PostOffice> offices = new HashMap<>();
    private static Warehouse centrum = new Warehouse();

    public static Mail getMailByTextId(String id) throws PostException, StorageException {
        return getStorage().getMailByTextId(id);
    }

    public static PostOffice getOfficeByPSC(int psc) throws StorageException, PostException {
        if(!offices.containsKey(psc)){
            offices.put(psc,getStorage().getOfficeByPSC(psc));
        }
        return offices.get(psc);
    }

    public static Warehouse getCenterWarehouse(){
        return centrum;
    }

    public static Mail create(MailContainer mc) throws StorageException {
        return getStorage().create(mc);
    }

    public static void changeMailStatus(Mail m) throws StorageException {
        getStorage().changeMailStatus(m);
    }

    public static void changeMailStatus(Status s, List<Integer> ids, int locationId) throws StorageException {
        getStorage().changeMailStatus(s,ids,locationId);
    }

    public static void changeMailStatus(Mail m, int officeId) throws StorageException {
        getStorage().changeMailStatus(m,officeId);
    }

    public static Mail initializeMail(MailContainer mc) throws PostException {
        if(!mc.isReady())  throw new IllegalArgumentException("MailContainer is not ready!");

        switch (mc.type){
            case "letter":
                return new Letter(
                        mc.id,mc.status,mc.sender,mc.receiverAddress,mc.receiverName,Letter.Type.valueOf(mc.info),mc.officeId
                );
            case "package":
                return new Package(
                        mc.id,mc.status,mc.sender,mc.receiverAddress,mc.receiverName, Package.Type.valueOf(mc.info),mc.officeId
                );
        }
        throw new PostException("Invalid mail type!");
    }

    public static List<Mail> filterMails(int userId, int psc) throws StorageException {
        return getStorage().getMailsWithFilter(userId,psc,null);
    }

    public static List<Mail> filterMails(int userId, int psc, Status s) throws StorageException {
        return getStorage().getMailsWithFilter(userId,psc,s);
    }

    public static List<Mail> filterMails(Status s) throws StorageException {
        return getStorage().getMailsWithFilter(-1,-1,s);
    }

}
