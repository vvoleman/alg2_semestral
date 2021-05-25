package cz.tul.vvoleman.app.post;

import cz.tul.vvoleman.app.post.mail.Letter;
import cz.tul.vvoleman.app.post.mail.Mail;
import cz.tul.vvoleman.app.post.mail.MailContainer;
import cz.tul.vvoleman.app.post.mail.Package;
import cz.tul.vvoleman.app.post.storage.PostStoreInterface;
import cz.tul.vvoleman.utils.exception.post.PostException;
import cz.tul.vvoleman.utils.exception.storage.StorageException;

import java.util.HashMap;
import java.util.Map;

public class PostLibrary {

    private static PostStoreInterface store;

    //PSC,PostOffice
    private static Map<Integer,PostOffice> offices = new HashMap<>();
    private static Warehouse centrum = new Warehouse();

    public static PostOffice getOfficeByPSC(int districtID){
        if(!offices.containsKey(districtID)){
            offices.put(districtID,store.getByPSC(districtID));
        }
        return offices.get(districtID);
    }

    public static Warehouse getCenterWarehouse(){
        return centrum;
    }

    public static Mail create(MailContainer mc) throws StorageException {
        return store.create(mc);
    }

    public static void changeMailStatus(Mail m) throws StorageException {
        store.changeMailStatus(m);
    }

    public static void changeMailStatus(Mail m, int officeId) throws StorageException {
        store.changeMailStatus(m,officeId);
    }

    public static Mail initializeMail(MailContainer mc) throws PostException {
        if(!mc.isReady())  throw new IllegalArgumentException("MailContainer is not ready!");

        switch (mc.type){
            case "letter":
                return new Letter(
                        mc.id,mc.status,mc.sender,mc.receiverAddress,mc.receiverName,Letter.Type.valueOf(mc.info)
                );
            case "package":
                return new Package(
                        mc.id,mc.status,mc.sender,mc.receiverAddress,mc.receiverName, Package.Type.valueOf(mc.info)
                );
        }
        throw new PostException("Invalid mail type!");
    }

}
