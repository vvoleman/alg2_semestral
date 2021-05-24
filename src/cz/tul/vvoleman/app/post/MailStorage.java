package cz.tul.vvoleman.app.post;

import cz.tul.vvoleman.app.post.mail.Mail;

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

    public List<Mail> filterByPsc(int psc, boolean remove, boolean include){
        return filter((m) -> m.getPSC() == psc && include,remove);
    }

    public List<Mail> filterByDistrict(int districtId, boolean remove, boolean include){
        return filter((m) -> m.getReceiverAddress().getDistrictId() == districtId && include,remove);
    }

    public List<Mail> filterByRegion(int regionId, boolean remove, boolean include){
        return filter((m) -> m.getReceiverAddress().getRegionId() == regionId && include,remove);
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
