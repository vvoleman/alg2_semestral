package cz.tul.vvoleman.app.post;

import cz.tul.vvoleman.app.post.mail.Mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class MailStorage {

    private List<Mail> mails;

    public MailStorage(){
        mails = new ArrayList<>();
    }

    public MailStorage(List<Mail> mails){
        this.mails = mails;
    }

    public void add(List<Mail> list){
        mails.addAll(list);
    }

    public void add(Mail mail){
        mails.add(mail);
    }

    public void add(MailTransport mailTransport){
        mails.addAll(mailTransport.getMails());
    }

    public int numberOfMails(){
        return mails.size();
    }

    public List<Mail> filterByPsc(int psc, boolean remove, boolean include){
        return filter((m) -> m.getPSC() == psc == include,remove);
    }

    public List<Mail> filterByDistrict(int districtId, boolean remove, boolean include){
        return filter((m) -> m.getReceiverAddress().getDistrictId() == districtId == include,remove);
    }

    public List<Mail> filterByRegion(int regionId, boolean remove, boolean include){
        return filter((m) -> m.getReceiverAddress().getRegionId() == regionId == include,remove);
    }

    public Map<Integer,List<Mail>> splitByPsc(){
        HashMap<Integer,List<Mail>> splitted = new HashMap<>();
        for (Mail m:mails) {
            if(!splitted.containsKey(m.getPSC())){
                ArrayList<Mail> list = new ArrayList<>();
                splitted.put(m.getPSC(),list);
            }
            splitted.get(m.getPSC()).add(m);
        }
        return splitted;
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

    public List<Mail> getMails(){
        return mails;
    }
}
