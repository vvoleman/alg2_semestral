package cz.tul.vvoleman.app.post;

import cz.tul.vvoleman.app.address.Address;
import cz.tul.vvoleman.app.post.mail.Mail;
import cz.tul.vvoleman.app.post.mail.Status;
import cz.tul.vvoleman.utils.exception.storage.StorageException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Warehouse implements PostalInterface{

    /**
     * Warehouse content
     */
    protected MailStorage mailStorage;

    public Warehouse(){
        try {
            mailStorage = new MailStorage(PostLibrary.filterMails(Status.Warehouse));
        } catch (StorageException e) {
            mailStorage = new MailStorage();
        }
    }

    @Override
    public void incomingTransport(MailTransport mailTransport) throws StorageException {
        List<Integer> ids = new ArrayList<>();

        for(Mail m : mailTransport.getMails()){
            ids.add(m.getId());
        }

        PostLibrary.changeMailStatus(Status.Warehouse,ids,-1);

        mailStorage.add(mailTransport);
    }

    @Override
    public int numberOfMails() {
        return mailStorage.numberOfMails();
    }

    public Map<Integer,List<Mail>> splitMailsByPSC(){
        return mailStorage.splitByPsc();
    }

    /**
     * Processes outgoing transport
     *
     * @param target PostalInterface
     */
    public void outgoingTransport(PostOffice target) throws StorageException {
        target.incomingTransport(new MailTransport(mailStorage.filterByPsc(target.getPsc(),true,true)));
    }

    public List<Mail> getMailList(){
        return mailStorage.getMails();
    }

}
