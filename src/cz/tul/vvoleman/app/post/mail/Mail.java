package cz.tul.vvoleman.app.post.mail;

import cz.tul.vvoleman.app.address.Address;
import cz.tul.vvoleman.app.auth.model.User;

public abstract class Mail {

    protected int id;
    protected Status status;
    protected User sender;
    protected Address receiverAddress;
    protected String receiverName;

    protected Mail(int id, Status status, User sender,Address receiverAddress, String receiverName) {
        this.id = id;
        this.status = status;
        this.sender = sender;
        this.receiverAddress = receiverAddress;
        this.receiverName = receiverName;
    }

    public Address getSenderAddress() {
        return sender.getAddress();
    }

    public Address getReceiverAddress() {
        return receiverAddress;
    }

    public String getSenderName() {
        return sender.getFirstname()+" "+sender.getLastname();
    }

    public String getReceiverName() {
        return receiverName;
    }

    public Status getStatus(){
        return status;
    }

    public int getPSC() {
        return receiverAddress.getPsc();
    }

    public int getId() {
        return id;
    }

    public abstract String getTextId();

}