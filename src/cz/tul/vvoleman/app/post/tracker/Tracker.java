package cz.tul.vvoleman.app.post.tracker;

import cz.tul.vvoleman.app.post.PostLibrary;
import cz.tul.vvoleman.app.post.mail.Mail;
import cz.tul.vvoleman.app.post.mail.Package;
import cz.tul.vvoleman.utils.exception.storage.StorageException;
import cz.tul.vvoleman.utils.tools.StringLibrary;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Tracker {

    private int byUser;
    private int byPSC;
    private Direction direction = Direction.ASC;
    private Order order = Order.Status;

    public Tracker setUser(int id) {
        byUser = id;
        return this;
    }

    public Tracker setPSC(int psc){
        byPSC = psc;
        return this;
    }

    public Tracker setOrderBy(Order order) {
        this.order = order;
        return this;
    }

    public List<Mail> get() throws StorageException {
        //Where: id,psc
        List<Mail> mails = PostLibrary.filterMails(byUser, byPSC);

        //zesortěný
        mails.sort(order.getComparator());
        if (direction == Direction.DESC) {
            mails.sort(Collections.reverseOrder());
        }


        return mails;
    }

    public String print() throws StorageException {
        List<Mail> mails = get();

        StringBuilder sb = new StringBuilder();

        String temp = getFormatedLine("ID", "Typ", "Odesílatel", "Adresát", "Adresa adresáta", "Status");
        sb.append(temp).append("\n");
        sb.append(StringLibrary.drawLine(temp.length(), '=')).append("\n");

        Mail m;
        for (int i = 0; i < mails.size(); i++) {
            m = mails.get(i);
            temp = (m instanceof Package) ? "Balíček" : "Psaní";
            sb.append(getFormatedLine(
                    m.getTextId(),
                    temp,
                    m.getSenderName(),
                    m.getReceiverName(),
                    m.getReceiverAddress().getFullAddress(),
                    m.getStatus().toString()
            ));
            if(i < mails.size()-1){
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    public enum Direction {
        ASC,
        DESC
    }

    private String getFormatedLine(String id, String type, String senderName, String receiverName, String receiverAddress, String status) {
        return String.format("%14s %14s %20s %20s %50s %15s", id, type, senderName, receiverName, receiverAddress, status);
    }

}
