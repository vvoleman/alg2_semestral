package cz.tul.vvoleman.app.post.storage;

import cz.tul.vvoleman.app.post.PostOffice;
import cz.tul.vvoleman.app.post.mail.Mail;
import cz.tul.vvoleman.app.post.mail.MailContainer;
import cz.tul.vvoleman.app.post.mail.Status;
import cz.tul.vvoleman.utils.exception.post.PostException;
import cz.tul.vvoleman.utils.exception.storage.StorageException;

import java.util.List;

public interface PostStoreInterface {

    /**
     * Returns Post Office by PSC
     * @param psc PSC
     * @return PostOffice
     */
    public PostOffice getOfficeByPSC(int psc) throws StorageException, PostException;

    /**
     * Creates Mail with MailContainer
     * @param mc MailContainer
     * @return Mail
     * @throws StorageException Can't access storage
     */
    public Mail create(MailContainer mc) throws StorageException;

    /**
     * Changes mail status
     * @param m Mail
     * @throws StorageException Can't access storage
     */
    public void changeMailStatus(Mail m) throws StorageException;

    /**
     * Changes mail status and location of mail
     * @param m Mail
     * @param officeId ID of PostOffice
     * @throws StorageException Can't access storage
     */
    public void changeMailStatus(Mail m,int officeId) throws StorageException;

    /**
     * Changes statuses and locations of mails
     * @param s Status
     * @param ids List of IDs to change
     * @param officeId PostOffice ID
     * @throws StorageException Can't access storage
     */
    public void changeMailStatus(Status s, List<Integer> ids,int officeId) throws StorageException;

    /**
     * Returns mail by with specified textID
     * @param textId TextID
     * @return Mail
     * @throws StorageException Can't access storage
     * @throws PostException No mail with this textID
     */
    public Mail getMailByTextId(String textId) throws StorageException, PostException;

    /**
     * Returns mails with filter
     * @param userId ID of user (-1 for not filtering by)
     * @param psc PSC (-1 for not filtering by)
     * @param status Status (null for not filtering by)
     * @return List of mails
     * @throws StorageException Can't access storage
     */
    public List<Mail> getMailsWithFilter(int userId, int psc, Status status) throws StorageException;

}
