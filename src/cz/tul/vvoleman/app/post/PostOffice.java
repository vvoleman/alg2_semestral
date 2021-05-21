package cz.tul.vvoleman.app.post;

import cz.tul.vvoleman.app.address.Address;

import java.util.List;
import java.util.Map;

public class PostOffice {

    private int psc;
    private Address address;
    private PostOffice controlOffice;

    private MailStorage mailStorage;
    private Map<Integer,PostOffice> controlledOffices;

    public PostOffice(int psc, Address address, PostOffice controlOffice) {
        this.psc = psc;
        this.address = address;
        this.controlOffice = controlOffice;

        mailStorage = new MailStorage();
    }

    public PostOffice(int psc, Address address, Map<Integer,PostOffice> controledOffices){
        this.psc = psc;
        this.address = address;
        this.controlledOffices = controledOffices;
    }
}
