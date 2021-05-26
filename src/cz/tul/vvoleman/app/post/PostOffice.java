package cz.tul.vvoleman.app.post;

import cz.tul.vvoleman.app.address.Address;
import cz.tul.vvoleman.app.post.mail.Mail;
import cz.tul.vvoleman.app.post.mail.Status;
import cz.tul.vvoleman.utils.exception.storage.StorageException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PostOffice implements PostalInterface{

    private int psc;
    private int id;
    private Address address;

    private MailStorage mailStorage;

    public PostOffice(int id, int psc, Address address) throws StorageException {
        this.psc = psc;
        this.id = id;
        this.address = address;

        mailStorage = new MailStorage(PostLibrary.filterMails(-1,psc));
    }

    public void incomingPersonalMail(Mail mail) throws StorageException {
        mail.setStatus((mail.getPSC() == psc) ? Status.ReceiverOffice : Status.SenderOffice);
        PostLibrary.changeMailStatus(mail,id);

        mailStorage.add(mail);
    }

    /**
     * Processes incoming transport
     *
     * @param mailTransport MailTransport
     */
    @Override
    public void incomingTransport(MailTransport mailTransport) throws StorageException {
        mailStorage.add(mailTransport);

        List<Integer> toHere = getIdsFromMails(mailStorage.filterByPsc(psc,false,true));
        PostLibrary.changeMailStatus(Status.ReceiverOffice,toHere,id);
    }

    //Všechno mimo naší psč

    /**
     * Sends MailTransport with all mails outside of our PSC to central warehouse
     * @throws StorageException Can't access storage
     */
    public void outgoingTransport() throws StorageException {
        PostLibrary.getCenterWarehouse().incomingTransport(new MailTransport(mailStorage.filterByPsc(psc,true,false)));
    }

    public void localOutgoingTransport() throws StorageException {
        List<Integer> list = getIdsFromMails(mailStorage.filterByPsc(psc,true,true));
        PostLibrary.changeMailStatus(Status.Delivered,list,-1);
    }

    @Override
    public int numberOfMails() {
        return mailStorage.numberOfMails();
    }

    public int numberOfOutgoing(){
        return mailStorage.filterByPsc(psc,false,false).size();
    }


    public int getPsc() {
        return psc;
    }

    public int getId(){
        return id;
    }

    private List<Integer> getIdsFromMails(List<Mail> mails){
        ArrayList<Integer> ids = new ArrayList<>();
        for(Mail m : mails){
            ids.add(m.getId());
        }
        return ids;
    }
}
