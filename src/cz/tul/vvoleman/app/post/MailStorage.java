package cz.tul.vvoleman.app.post;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class MailStorage {

    private List<Mail> mails;

    public MailStorage(){
        mails = new ArrayList<>();
    }

    public void add(Mail mail){
        mails.add(mail);
    }

    public void add(MailTransport mailTransport){
        mails.addAll(mailTransport.getMails());
    }

    public List<Mail> filterByPsc(int psc, boolean remove){
        return filter((m) -> m.getPSC() == psc,remove);
    }

    /**
     * Universal filter for storage
     * @param p Predicate<Mail>
     * @param remove Should I remove found item?
     * @return List of filtered mails
     */
    private List<Mail> filter(Predicate<Mail> p, boolean remove){
        List<Mail> out = new ArrayList<>();
        for (int i = mails.size()-1; i >= 0; i--) {
            Mail m = mails.get(i);
            if (p.test(m)) {
                out.add(mails.get(i));

                if(remove){
                    mails.remove(i);
                }
            }
        }

        return out;
    }
}