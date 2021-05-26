package cz.tul.vvoleman.app.post.mail;

import cz.tul.vvoleman.app.address.Address;
import cz.tul.vvoleman.app.auth.model.User;

import java.time.LocalDateTime;

public abstract class Mail {

    protected int id;
    protected LocalDateTime lastChangedAt;
    protected Status status;
    protected int locationId;
    protected User sender;
    protected Address receiverAddress;
    protected String receiverName;

    protected Mail(int id, Status status, User sender, Address receiverAddress, String receiverName, int locationId, LocalDateTime lastChangedAt) {
        this.id = id;
        this.status = status;
        this.sender = sender;
        this.receiverAddress = receiverAddress;
        this.receiverName = receiverName;
        this.locationId = locationId;
        this.lastChangedAt = lastChangedAt;
    }

    public int getSenderId(){
        return sender.getId();
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

    public void setStatus(Status status){
        this.status = status;
    }

    public int getPSC() {
        return receiverAddress.getPsc();
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getLastChangedAt() {
        return lastChangedAt;
    }

    public int getLocationId(){
        return locationId;
    }

    public void setLocationId(int locationId){
        this.locationId = locationId;
    }

    public abstract String getTypeString();

    public abstract String getInfoString();

    public abstract String getTextId();

}
