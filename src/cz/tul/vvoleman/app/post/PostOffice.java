package cz.tul.vvoleman.app.post;

import cz.tul.vvoleman.app.address.Address;
import cz.tul.vvoleman.app.post.mail.Mail;
import cz.tul.vvoleman.utils.exception.storage.StorageException;

import java.util.List;
import java.util.Map;

public class PostOffice{

    private int psc;
    private int id;
    private Address address;

    private MailStorage mailStorage;

    public PostOffice(int psc, int id, Address address){
        this.psc = psc;
        this.id = id;
        this.address = address;

        mailStorage = new MailStorage();
    }

    public void incomingPersonalMail(Mail mail){
        mailStorage.add(mail);
        //Změnit stav na senderOffice
    }

    /**
     * Processes incoming transport
     *
     * @param mailTransport MailTransport
     */
    public void incomingTransport(MailTransport mailTransport) throws StorageException {
        mailStorage.add(mailTransport);

        List<Mail> toHere = mailStorage.filterByPsc(psc,false,true);
        for (Mail m:toHere){
            PostLibrary.changeMailStatus(m,id);
        }
    }

    //Všechno mimo naší psč
    public void outgoingTransport(){
        PostLibrary.getCenterWarehouse().incomingTransport(new MailTransport(mailStorage.filterByPsc(psc,true,false)));
    }

    public int numberOfOutgoing(){
        return mailStorage.filterByPsc(psc,false,false).size();
    }


    public int getPsc() {
        return psc;
    }
}
