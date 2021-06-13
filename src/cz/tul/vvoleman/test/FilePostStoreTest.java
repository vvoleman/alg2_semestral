package cz.tul.vvoleman.test;

import cz.tul.vvoleman.app.address.AddressLibrary;
import cz.tul.vvoleman.app.auth.Auth;
import cz.tul.vvoleman.app.post.PostOffice;
import cz.tul.vvoleman.app.post.mail.Mail;
import cz.tul.vvoleman.app.post.mail.MailContainer;
import cz.tul.vvoleman.app.post.mail.Status;
import cz.tul.vvoleman.app.post.storage.FilePostStore;
import cz.tul.vvoleman.resource.Datastore;
import cz.tul.vvoleman.utils.exception.address.BadAddressFormatException;
import cz.tul.vvoleman.utils.exception.auth.UnknownUserException;
import cz.tul.vvoleman.utils.exception.post.PostException;
import cz.tul.vvoleman.utils.exception.storage.StorageException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilePostStoreTest {

    private FilePostStore fps = new FilePostStore();

    @Test
    @DisplayName("Získání adresy podle PSČ")
    @Disabled
    void getOfficeByPSC() throws PostException, StorageException {

        int psc = 40003;
        PostOffice po = fps.getOfficeByPSC(psc);

        assertEquals(1,po.getId());
    }

    @Test
    @DisplayName("Mail podle textID")
    @Disabled
    void getMailByTextId() throws PostException, StorageException {
        String textID = "CZLB0000000001";
        Mail m = fps.getMailByTextId(textID);

        assertEquals(textID,m.getTextId());
    }

    @Test
    @DisplayName("Filtr")
    @Disabled
    void filter() throws StorageException {
        List<Mail> list = fps.getMailsWithFilter(1,-1,Status.Registered);
        assertEquals(1,list.size());
    }

    @Test
    @DisplayName("Vytvoření zásilky")
    @Disabled
    void create() throws UnknownUserException, StorageException, SQLException, BadAddressFormatException {
        MailContainer mc = new MailContainer(
                Status.Registered, Auth.getUser(1), AddressLibrary.getAddressByInput("Krupá 114 27009"),
                "Vojtěch Falný","package","L"
        );

        Mail m = fps.create(mc);
        assertEquals(1,m.getSenderId());
    }

    @Test
    @DisplayName("Nahrazení souboru")
    @Disabled
    void line() throws IOException {
        fps.replaceLine(new File(Datastore.getMailStorageFile()),(p) -> p[0].equalsIgnoreCase("2"),"Surprise!");
    }
}