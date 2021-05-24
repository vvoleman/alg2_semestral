package cz.tul.vvoleman.app.post;

public interface PostalInterface {

    public void incomingTransport(MailTransport mailTransport);

    public void outgoingTransport(PostalInterface pi);

}
