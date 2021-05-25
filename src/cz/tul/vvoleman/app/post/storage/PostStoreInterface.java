package cz.tul.vvoleman.app.post.storage;

import cz.tul.vvoleman.app.post.PostOffice;
import cz.tul.vvoleman.app.post.mail.Mail;
import cz.tul.vvoleman.app.post.mail.MailContainer;
import cz.tul.vvoleman.utils.exception.storage.StorageException;

public interface PostStoreInterface {

    /**
     * Returns Post Office by PSC
     * @param psc
     * @return
     */
    public PostOffice getByPSC(int psc);

    public Mail create(MailContainer mc) throws StorageException;

    public void changeMailStatus(Mail m) throws StorageException;

    public void changeMailStatus(Mail m,int officeId) throws StorageException;

    //public PostOffice getByDistrict(int districtId);

}
