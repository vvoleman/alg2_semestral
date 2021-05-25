package cz.tul.vvoleman.app.post.tracker;

import cz.tul.vvoleman.app.post.mail.Mail;

import java.util.Comparator;

public enum Order {

    Status((Mail m1, Mail m2) -> m1.getStatus().getNumber()-m2.getStatus().getNumber()),
    User((Mail m1, Mail m2) -> m1.getSenderName().compareTo(m2.getSenderName()));

    private final Comparator<Mail> comparator;

    public Comparator<Mail> getComparator(){
        return comparator;
    }

    Order(Comparator<Mail> comparator){
        this.comparator = comparator;
    }

}
