package cz.tul.vvoleman.app.post;

import java.util.List;

public class MailTransport {

    private final List<Mail> mails;

    public MailTransport(List<Mail> mails) {
        this.mails = mails;
    }

    public List<Mail> getMails(){
        return mails;
    }

}
