package cz.tul.vvoleman.app.post;

import cz.tul.vvoleman.app.address.Address;

public class Warehouse {

    /**
     * Warehouse info
     */
    private final String name;
    private final int id;
    private final Address address;

    /**
     * Warehouse content
     */
    private MailStorage mailStorage;

    public Warehouse(String name, int id, Address address){
        this.name = name;
        this.id = id;
        this.address = address;

        mailStorage = new MailStorage();
    }

    public void incomingTransport(MailTransport mailTransport){
        mailStorage.add(mailTransport);
    }

}
