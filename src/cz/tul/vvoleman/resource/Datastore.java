package cz.tul.vvoleman.resource;

import cz.tul.vvoleman.app.auth.storage.DatabaseAuthStorage;
import cz.tul.vvoleman.app.auth.storage.FileAuthStorage;
import cz.tul.vvoleman.app.post.storage.DatabasePostStore;
import cz.tul.vvoleman.app.post.storage.FilePostStore;
import cz.tul.vvoleman.io.Storage;
import cz.tul.vvoleman.io.StorageContainer;
import cz.tul.vvoleman.utils.exception.storage.StorageException;

import java.time.format.DateTimeFormatter;

public class Datastore {

    private final static Storage storageUsed = Storage.Database;
    private static StorageContainer sc;

    //////////////////////////////////////////////////////////////

    private final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final static String dataFolder = "data";
    private final static String separator = System.getProperty("file.separator");
    private final static String delimiter = ",";
    private final static String fileStorageFolder = "storage";
    private final static String postStorageFile = "post_offices.csv";
    private final static String mailStorageFile = "mails.csv";
    private final static String authStorageFile = "users.csv";
    private final static String[] authColumns = {"id", "email", "password", "firstname", "lastname", "address_id", "created_at", "enabled", "role"};
    private final static String[] postColumns = {"id","psc","address_id"};
    private final static String[] mailColumns = {"id","sender_id","text_id","location_id","receiver_address_id","receiver_name","status","type","info","last_changed_at"};
    //////////////////////////////////////////////////////////////

    private final static DBContainer addresses = new DBContainer(
            "localhost", 3306, "adresy", "root", "", "mariadb"
    );

    private final static DBContainer datas = new DBContainer(
            "localhost", 3306, "alg2_semestral", "root", "", "mariadb"
    );

    //////////////////////////////////////////////////////////////

    public static DateTimeFormatter getDtf() {
        return dtf;
    }

    public static String getDataFolder() {
        return dataFolder;
    }

    public static String getFileStorageFolder() {
        return dataFolder + separator + fileStorageFolder;
    }

    public static String getAuthStorageFile() {
        return getFileStorageFolder() + separator + authStorageFile;
    }

    public static String getPostStorageFile() {
        return getFileStorageFolder() + separator + postStorageFile;
    }

    public static String getMailStorageFile() {
        return getFileStorageFolder() + separator + mailStorageFile;
    }

    public static String getDelimiter() {
        return delimiter;
    }

    public static String[] getAuthColumns() {
        return authColumns;
    }

    public static int getAuthColumnsSize() {
        return authColumns.length;
    }

    public static int getPostColumnSize(){
        return postColumns.length;
    }

    public static int getMailColumnSize(){
        return mailColumns.length;
    }

    public static DBContainer getAddresses() {
        return addresses;
    }

    public static DBContainer getDatas() {
        return datas;
    }

    public static StorageContainer getStorageContainer() throws StorageException {
        if (sc == null) {
            switch (storageUsed) {
                case Database:
                    sc = new StorageContainer(new DatabaseAuthStorage(),new DatabasePostStore());
                    break;
                default:
                    sc = new StorageContainer(new FileAuthStorage(),new FilePostStore());
                    break;
            }

        }
        return sc;
    }

    public static Storage getStorageUsed(){
        return storageUsed;
    }
}
