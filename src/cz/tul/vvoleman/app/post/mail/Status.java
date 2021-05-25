package cz.tul.vvoleman.app.post.mail;

public enum Status {

    Registered(1),
    SenderOffice(2),
    Warehouse(3),
    ReceiverOffice(4),
    Delivered(5);

    private final int number;

    Status(int number){
        this.number = number;
    }

    public int getNumber(){
        return number;
    }

}
