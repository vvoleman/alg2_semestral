package cz.tul.vvoleman.resource;

public class DBContainer {

    public String hostname;
    public int port;
    public String dbname;
    public String username;
    public String password;
    public String connector;

    public DBContainer(String hostname, int port, String dbname, String username, String password, String connector) {
        this.hostname = hostname;
        this.port = port;
        this.dbname = dbname;
        this.username = username;
        this.password = password;
        this.connector = connector;
    }
}
