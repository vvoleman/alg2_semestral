package cz.tul.vvoleman.app.post;

import cz.tul.vvoleman.app.address.Address;

import java.util.List;
import java.util.Map;

public class PostOffice{

    private int psc;
    private Address address;

    private MailStorage mailStorage;

    public PostOffice(int psc, Address address){
        this.psc = psc;
        this.address = address;

        mailStorage = new MailStorage();
    }

    /**
     * Processes incoming transport
     *
     * @param mailTransport MailTransport
     */
    public void incomingTransport(MailTransport mailTransport) {
        mailStorage.add(mailTransport);
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
