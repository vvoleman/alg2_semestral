package cz.tul.vvoleman.ui.section;

import cz.tul.vvoleman.app.post.mail.Mail;
import cz.tul.vvoleman.app.post.tracker.Order;
import cz.tul.vvoleman.app.post.tracker.Tracker;
import cz.tul.vvoleman.ui.Menu;
import cz.tul.vvoleman.ui.Section;
import cz.tul.vvoleman.utils.exception.post.PostException;
import cz.tul.vvoleman.utils.exception.storage.StorageException;

import java.util.List;
import java.util.Scanner;

public class PostTracker extends Section {

    private Tracker t;
    private Filter f;
    private int info;
    private Order[] optionsUser = {Order.Status,Order.Receiver};
    private Order[] optionsPSC = {Order.Status,Order.Receiver,Order.Sender};

    public PostTracker(Scanner sc,Filter f,int info) {
        super(sc);
        t = new Tracker();
        this.f = f;
        this.info = info;
    }

    public PostTracker(Scanner sc){
        super(sc);
        t = new Tracker();
    }

    @Override
    public boolean run() {
        Order[] used = optionsPSC;
        List<Mail> mails;
        if(f != null){
            if(f == Filter.PSC){
                t.setPSC(info);
            }else{
                used = optionsUser;
                t.setUser(info);
            }

            Menu m = new Menu(getOrderNames(used));

            System.out.println("Podle čeho chcete řadit?");
            m.drawMenuUI();

            int sel = m.chooseOption();

            t.setOrderBy(used[sel-1]);

            try {
                mails = t.get();
            } catch (StorageException e) {
                System.out.println("Nelze načíst zásilky, zkuste to prosím později!");
                return true;
            }

        }else{
            System.out.print("Zadejte číslo balíčku: ");
            t.setTextId(sc.next());

            try {
                mails = t.getByTextId();
            } catch (StorageException e) {
                System.out.println("Nelze načíst zásilky, zkuste to prosím později!");
                return true;
            } catch (PostException e) {
                System.out.println("Zadané číslo balíčku neexistuje!");
                return true;
            }
        }

        System.out.println(t.print(mails));
        System.out.println();

        return true;
    }

    public void externalData(List<Mail> mailList){
        Menu m = new Menu(getOrderNames(optionsPSC));

        System.out.println("Podle čeho chcete řadit?");
        m.drawMenuUI();

        int sel = m.chooseOption();

        mailList.sort(optionsPSC[sel-1].getComparator());

        System.out.println(t.print(mailList));
        System.out.println();
    }

    public enum Filter{
        User,
        PSC
    }

    private String[] getOrderNames(Order[] orders){
        String[] names = new String[orders.length];
        for (int i = 0; i < orders.length; i++) {
            names[i] = orders[i].getName();
        }
        return names;
    }

}
