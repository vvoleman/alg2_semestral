package cz.tul.vvoleman.app.post.storage;

import cz.tul.vvoleman.app.address.AddressLibrary;
import cz.tul.vvoleman.app.auth.Auth;
import cz.tul.vvoleman.app.post.PostLibrary;
import cz.tul.vvoleman.app.post.PostOffice;
import cz.tul.vvoleman.app.post.mail.Mail;
import cz.tul.vvoleman.app.post.mail.MailContainer;
import cz.tul.vvoleman.app.post.mail.Status;
import cz.tul.vvoleman.io.TextFileReader;
import cz.tul.vvoleman.io.TextFileWriter;
import cz.tul.vvoleman.resource.Datastore;
import cz.tul.vvoleman.utils.exception.auth.UnknownUserException;
import cz.tul.vvoleman.utils.exception.post.PostException;
import cz.tul.vvoleman.utils.exception.storage.StorageException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class FilePostStore implements PostStoreInterface {

    private static Map<Integer, PostOffice> offices = new HashMap<>();

    private final File postFile = new File(Datastore.getPostStorageFile());
    private final File mailFile = new File(Datastore.getMailStorageFile());

    /**
     * Returns Post Office by PSC
     *
     * @param psc PSC
     * @return PostOffice
     */
    @Override
    public PostOffice getOfficeByPSC(int psc) throws StorageException, PostException {
        if(!offices.containsKey(psc)){
            //Načteme data ze souborů, která obsahují PSČ
            List<String[]> data = extendedGet(postFile, (p) -> p[1].equalsIgnoreCase(Integer.toString(psc)), 1);

            if (data.size() == 0) {
                throw new PostException("There was no post with this PSC!");
            }
            PostOffice po = getOfficeFromParts(data.get(0));
            offices.put(psc,po);
            po.addListToStorage(getMailsWithFilter(-1,psc,null));
        }
        return offices.get(psc);
    }

    /**
     * Creates Mail with MailContainer
     *
     * @param mc MailContainer
     * @return Mail
     * @throws StorageException Can't access storage
     */
    @Override
    public Mail create(MailContainer mc) throws StorageException {
        mc.id = getLastID(mailFile) + 1;
        mc.lastChangedAt = LocalDateTime.now();

        try {
            TextFileWriter.writeToFile(mailFile, mailContainerToLine(mc));

            return PostLibrary.initializeMail(mc);
        } catch (IOException | PostException e) {
            throw new StorageException("Unable to create new mail!");
        }
    }

    /**
     * Changes mail status
     *
     * @param m Mail
     * @throws StorageException Can't access storage
     */
    @Override
    public void changeMailStatus(Mail m) throws StorageException {
        changeMailStatus(m,-1);
    }

    /**
     * Changes mail status and location of mail
     *
     * @param m        Mail
     * @param officeId ID of PostOffice
     * @throws StorageException Can't access storage
     */
    @Override
    public void changeMailStatus(Mail m, int officeId) throws StorageException {
        m.setLocationId(officeId);
        try{
            String line = mailContainerToLine(m);
            replaceLine(mailFile,(p) -> p[0].equalsIgnoreCase(""+m.getId()),line);
        } catch (PostException | IOException e) {
            throw new StorageException("Unable to change mail status!");
        }
    }

    /**
     * Changes statuses and locations of mails
     *
     * @param s        Status
     * @param ids      List of IDs to change
     * @param officeId PostOffice ID
     * @throws StorageException Can't access storage
     */
    @Override
    public void changeMailStatus(Status s, List<Integer> ids, int officeId) throws StorageException {
        Mail m;
        List<String[]> data;
        Predicate<String[]> filter;
        for (int i:ids) {
            filter = (p) -> p[0].equalsIgnoreCase(""+i);
            data = extendedGet(mailFile,filter,1);

            if(data.size() == 0){
                throw new StorageException("Unable to change status for ID="+i);
            }

            try {
                m = getMailFromParts(data.get(0));
                m.setStatus(s);
                m.setLocationId(officeId);
                replaceLine(mailFile,filter,mailContainerToLine(m));
            } catch (PostException | IOException e) {
                throw new StorageException("Unable to change status for ID="+i);
            }
        }
    }

    /**
     * Returns mail by with specified textID
     *
     * @param textId TextID
     * @return Mail
     * @throws StorageException Can't access storage
     * @throws PostException    No mail with this textID
     */
    @Override
    public Mail getMailByTextId(String textId) throws StorageException, PostException {
        List<String[]> data = extendedGet(mailFile, (p) -> p[2].equalsIgnoreCase(textId), 1);

        if (data.size() == 0) {
            throw new PostException("No mail with this textID");
        }

        return getMailFromParts(data.get(0));
    }

    /**
     * Returns mails with filter
     *
     * @param userId ID of user (-1 for not filtering by)
     * @param psc    PSC (-1 for not filtering by)
     * @param status Status (null for not filtering by)
     * @return List of mails
     * @throws StorageException Can't access storage
     */
    @Override
    public List<Mail> getMailsWithFilter(int userId, int psc, Status status) throws StorageException {
        boolean bUser = userId > 0;
        boolean bPsc = psc > 0;
        boolean bStatus = status != null;
        try {
            PostOffice po = null;
            if (bPsc) {
                po = this.getOfficeByPSC(psc);
            }
            PostOffice finalPo = po;
            List<String[]> data = extendedGet(mailFile,
                    (p) -> ((p[1].equalsIgnoreCase("" + userId) || !bUser) &&
                            (!bPsc || (finalPo != null && p[3].equalsIgnoreCase("" + finalPo.getId()))) &&
                            (!bStatus || p[6].equalsIgnoreCase(status.toString()))
                    ), 1);

            List<Mail> list = new ArrayList<>();
            for(String[] parts : data){
                list.add(getMailFromParts(parts));
            }

            return list;

        } catch (PostException e) {
            throw new StorageException("Unable to filter mails!");
        }
    }

    ////////////////////////////////////////////////////////////////

    private PostOffice getOfficeFromParts(String[] parts) throws StorageException {
        if (parts.length != Datastore.getPostColumnSize()) {
            throw new StorageException("Bad format of storage!");
        }

        try {
            return new PostOffice(
                    Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1]),
                    AddressLibrary.getAddressById(Integer.parseInt(parts[2]))
                    );
        } catch (SQLException e) {
            throw new StorageException("Unable to load post office!");
        }

    }

    private Mail getMailFromParts(String[] parts) throws StorageException, PostException {
        if (parts.length != Datastore.getMailColumnSize()) {
            throw new StorageException("Bad format of storage!");
        }

        try {
            MailContainer mc = new MailContainer(
                    //id
                    Integer.parseInt(parts[0]),
                    //status
                    Status.valueOf(parts[6]),
                    //sender
                    Auth.getUser(Integer.parseInt(parts[1])),
                    //receiver_addr
                    AddressLibrary.getAddressById(Integer.parseInt(parts[4])),
                    //receiver_name
                    parts[5],
                    //type
                    parts[7],
                    //info
                    parts[8]
            );
            mc.lastChangedAt = LocalDateTime.parse(parts[9], Datastore.getDtf());

            return PostLibrary.initializeMail(mc);
        } catch (SQLException | UnknownUserException e) {
            throw new StorageException("Unable to read mail!");
        }
    }

    private List<String[]> extendedGet(File f, Predicate<String[]> filter, int limit) throws StorageException {
        List<String[]> data;

        try {
            data = TextFileReader.readWithFilter(f, Datastore.getDelimiter(), true, filter, limit);
        } catch (IOException e) {
            throw new StorageException("Unable to access storage!");
        }

        return data;
    }

    private int getLastID(File f) throws StorageException {
        try {
            return Integer.parseInt(TextFileReader.readLastLine(f, Datastore.getDelimiter())[0]);
        } catch (IOException e) {
            throw new StorageException("Unable to read storage!");
        } catch (Exception e) {
            return 0;
        }
    }

    private String mailContainerToLine(MailContainer mc) throws PostException {
        if (!mc.isReady()) throw new PostException("Unable to create mail!");

        return String.format("%d,%d,%s,%s,%d,%s,%s,%s,%s,%s",
                mc.id, mc.sender.getId(), PostLibrary.makeTextId(mc.id, mc.type, mc.info),
                mc.officeId > 0 ? mc.officeId : "", mc.receiverAddress.getId(), mc.receiverName,
                mc.status.toString(), mc.type, mc.info, mc.lastChangedAt.format(Datastore.getDtf())
        );
    }

    private String mailContainerToLine(Mail m) throws PostException {


        return String.format("%d,%d,%s,%s,%d,%s,%s,%s,%s,%s",
                m.getId(), m.getSenderId(), m.getTextId(),
                m.getLocationId() > 0 ? m.getLocationId() : "", m.getReceiverAddress().getId(), m.getReceiverName(),
                m.getStatus().toString(), m.getTypeString(), m.getInfoString(), m.getLastChangedAt().format(Datastore.getDtf())
        );
    }

    public void replaceLine(File f, Predicate<String[]> filter,String newLine) throws IOException {
        int line = TextFileReader.getIndexOfLine(f,",",true,filter);
        List<String> allLines = TextFileReader.readFileLines(f);
        allLines.set(line,newLine);
        TextFileWriter.writeToFile(f,allLines,false);
    }
}
