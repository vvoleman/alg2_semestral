package cz.tul.vvoleman.app.post;

import cz.tul.vvoleman.utils.exception.storage.StorageException;

public interface PostalInterface {

    public void incomingTransport(MailTransport mailTransport) throws StorageException;

    public int numberOfMails();

}
