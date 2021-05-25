package cz.tul.vvoleman.app.post;

import cz.tul.vvoleman.app.auth.Auth;
import cz.tul.vvoleman.app.auth.storage.AuthStoreInterface;
import cz.tul.vvoleman.app.post.mail.Letter;
import cz.tul.vvoleman.app.post.mail.Mail;
import cz.tul.vvoleman.app.post.mail.MailContainer;
import cz.tul.vvoleman.app.post.mail.Package;
import cz.tul.vvoleman.app.post.storage.PostStoreInterface;
import cz.tul.vvoleman.resource.Datastore;
import cz.tul.vvoleman.utils.exception.post.PostException;
import cz.tul.vvoleman.utils.exception.storage.StorageException;

import java.util.HashMap;
import java.util.Map;

public class PostLibrary {

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

    //PSC,PostOffice
    private static Map<Integer,PostOffice> offices = new HashMap<>();
    private static Warehouse centrum = new Warehouse();

    public static Mail getMailByTextId(String id) throws PostException, StorageException {
        return getStorage().getMailByTextId(id);
    }

    public static PostOffice getOfficeByPSC(int districtID) throws StorageException {
        if(!offices.containsKey(districtID)){
            offices.put(districtID,getStorage().getOfficeByPSC(districtID));
        }
        return offices.get(districtID);
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

}
