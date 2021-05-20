package cz.tul.vvoleman.resource;

import java.time.format.DateTimeFormatter;

public class Datastore {

    private final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final static String dataFolder = "data";
    private final static String separator = System.getProperty("file.separator");
    private final static String delimiter = ",";
    private final static String fileStorageFolder = "storage";
    private final static String authStorageFile = "users.csv";
    private final static String[] authColumns = {"id","email","password","firstname","lastname","address_id","created_at","enabled","role"};

    public static DateTimeFormatter getDtf() {
        return dtf;
    }

    public static String getDataFolder() {
        return dataFolder;
    }

    public static String getFileStorageFolder() {
        return dataFolder+separator+fileStorageFolder;
    }

    public static String getAuthStorageFile() {
        return getFileStorageFolder()+separator+authStorageFile;
    }

    public static String getDelimiter() {
        return delimiter;
    }

    public static String[] getAuthColumns() {
        return authColumns;
    }

    public static int getAuthColumnsSize(){
        return authColumns.length;
    }
}