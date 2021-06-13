package cz.tul.vvoleman.utils.tools;

import cz.tul.vvoleman.io.Database;
import cz.tul.vvoleman.io.TextFileReader;
import cz.tul.vvoleman.io.TextFileWriter;
import cz.tul.vvoleman.resource.Datastore;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FromDBtoFiles {

    private static Connection dbApp;
    private static File postFile = new File(Datastore.getPostStorageFile());
    private static File authFile = new File(Datastore.getAuthStorageFile());
    private static File mailFile = new File(Datastore.getMailStorageFile());

    static {
        try {
            dbApp = Database.getConn(Datastore.getDatas());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
        postOffices();
        System.out.println("PostOffice hotovo");
        users();
        System.out.println("Uživatelé hotovo");
        mails();
        System.out.println("Zásilky hotovo");
    }

    private static void postOffices() throws SQLException, IOException {
        List<String> list = new ArrayList<>();
        PreparedStatement ps = dbApp.prepareStatement("SELECT id,psc,address_id FROM post_offices");
        ResultSet rs = ps.executeQuery();
        list.add(TextFileReader.readFileLines(postFile).get(0));
        while (rs.next()) {
            list.add(String.format("%d,%d,%d", rs.getInt(1), rs.getInt(2), rs.getInt(3)));
        }
        TextFileWriter.writeToFile(postFile, list, false);
    }

    private static void users() throws SQLException, IOException {
        List<String> list = new ArrayList<>();
        PreparedStatement ps = dbApp.prepareStatement("SELECT * FROM users");
        ResultSet rs = ps.executeQuery();
        list.add(TextFileReader.readFileLines(authFile).get(0));
        while (rs.next()) {
            list.add(String.format("%d,%s,%s,%s,%s,%d,%s,%d,%s",
                    rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getString(4),rs.getString(5),rs.getInt(6),
                    rs.getTimestamp(7).toLocalDateTime().format(Datastore.getDtf()),rs.getInt(8),rs.getString(9)
                    )
            );
        }
        TextFileWriter.writeToFile(authFile, list, false);
    }

    private static void mails() throws SQLException, IOException {
        List<String> list = new ArrayList<>();
        PreparedStatement ps = dbApp.prepareStatement("SELECT * FROM mails");
        ResultSet rs = ps.executeQuery();
        list.add(TextFileReader.readFileLines(mailFile).get(0));
        while (rs.next()) {
            list.add(String.format("%d,%d,%s,%d,%d,%s,%s,%s,%s,%s",
                    rs.getInt(1), rs.getInt(2), rs.getString(3),
                    rs.getInt(4),rs.getInt(5),rs.getInt(6),
                    rs.getString(7),rs.getInt(8),rs.getString(9),
                    rs.getTimestamp(10).toLocalDateTime().format(Datastore.getDtf())
                    )
            );
        }
        TextFileWriter.writeToFile(mailFile, list, false);
    }

}
