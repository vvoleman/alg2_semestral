package cz.tul.vvoleman.app.post.mail;

import cz.tul.vvoleman.app.address.Address;
import cz.tul.vvoleman.app.auth.model.User;

public class MailContainer {

    public int id = -1;
    public Status status;
    public User sender;
    public Address receiverAddress;
    public String receiverName;
    public String type;
    public int officeId;
    public String info;

    public MailContainer(Status status, User sender, Address receiverAddress, String receiverName, String type, String info) {
        this.status = status;
        this.sender = sender;
        this.receiverAddress = receiverAddress;
        this.receiverName = receiverName;
        this.type = type;
        this.info = info;
    }

    public MailContainer(int id, Status status, User sender, Address receiverAddress, String receiverName, String type, String info) {
        this.id = id;
        this.status = status;
        this.sender = sender;
        this.receiverAddress = receiverAddress;
        this.receiverName = receiverName;
        this.type = type;
        this.info = info;
    }

    public boolean isReady(){
        return id != -1 && status != null && receiverAddress != null && receiverName != null && type != null && info != null;
    }
}
