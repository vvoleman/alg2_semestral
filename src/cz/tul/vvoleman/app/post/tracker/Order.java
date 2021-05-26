package cz.tul.vvoleman.app.post.tracker;

import cz.tul.vvoleman.app.post.mail.Mail;

import java.util.Comparator;

public enum Order {

    Status("Status",(Mail m1, Mail m2) -> m1.getStatus().getNumber()-m2.getStatus().getNumber()),
    Sender("Odesílatel",(Mail m1, Mail m2) -> m1.getSenderName().compareTo(m2.getSenderName())),
    Receiver("Adresát",(Mail m1, Mail m2) -> m1.getReceiverName().compareTo(m2.getReceiverName()));

    private final Comparator<Mail> comparator;
    private final String name;

    public Comparator<Mail> getComparator(){
        return comparator;
    }

    public String getName(){
        return name;
    }

    Order(String name,Comparator<Mail> comparator){
        this.name = name;
        this.comparator = comparator;
    }

}
