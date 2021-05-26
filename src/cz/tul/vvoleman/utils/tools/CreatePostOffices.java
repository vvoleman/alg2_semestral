package cz.tul.vvoleman.utils.tools;

import cz.tul.vvoleman.io.Database;
import cz.tul.vvoleman.resource.Datastore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Creates PostOffices for all PSČ
 */
public class CreatePostOffices {

    private static Connection dbAdresy;
    private static Connection dbApp;

    static {
        try {
            dbAdresy = Database.getConn(Datastore.getAddresses());
            dbApp = Database.getConn(Datastore.getDatas());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        //Načíst všechna PSČ a vytvořit pro ně
        Map<Integer,Integer> mapPsc = loadPSC();
        System.out.println("PSČ načtena");
        createOffices(mapPsc);
        System.out.println("Hotovo!");
    }

    private static Map<Integer,Integer> loadPSC() throws SQLException {
        String query = "SELECT psc, d.kod as id FROM casti_obci co JOIN domy d ON d.kod_casti_obce = co.kod GROUP BY psc";
        PreparedStatement ps = dbAdresy.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        Map<Integer,Integer> mapa = new HashMap<>();
        while(rs.next()){
            mapa.put(rs.getInt(1),rs.getInt(2));
        }
        return mapa;
    }

    private static void createOffices(Map<Integer,Integer> map) throws SQLException {
        //smaž všechny offices
        PreparedStatement ps = dbApp.prepareStatement("DELETE FROM post_offices");
        ps.executeUpdate();
        ps = dbApp.prepareStatement("ALTER TABLE post_offices AUTO_INCREMENT = 1;");
        ps.executeUpdate();

        List<Integer> keys = new ArrayList<>(map.keySet());
        String query;
        int max = keys.size();
        for (int i = 0; i < max; i++) {
            query = "INSERT INTO post_offices (psc,address_id) VALUES(?,?)";
            ps = dbApp.prepareStatement(query);
            ps.setInt(1,keys.get(i));
            ps.setInt(2,map.get(keys.get(i)));
            ps.executeUpdate();
            System.out.println((i+1)+"/"+max+" hotovo");
        }
    }

}
